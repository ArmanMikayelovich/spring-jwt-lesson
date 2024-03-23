import React, {useEffect, useState} from "react";
import {useForm} from "react-hook-form";
import {deleteFile, getAllFileInfos, getAllFileInfosByUser} from "../restService/FileRestService";
import {UsernameById} from "../user/UsernameById";
import {HOST_ADDRESS} from "../restService/consts";
import {getTokenFromLocalStorage, getUsername} from "../auth/Auth";
import {Document, Page, pdfjs} from "react-pdf";
import Typography from "@material-ui/core/Typography";
import PictureAsPdfIcon from '@material-ui/icons/PictureAsPdf';
import Paper from "@material-ui/core/Paper";
import {getAllUsers} from "../restService/UserRestService";


pdfjs.GlobalWorkerOptions.workerSrc = `//cdnjs.cloudflare.com/ajax/libs/pdf.js/${pdfjs.version}/pdf.worker.js`;

function getDateTimeFromTimeStamp(time) {
    return `${time.year}/${time.monthValue}/${time.dayOfMonth} ${time.hour} : ${time.minute} : ${time.minute}`
}


export function ManagerPage() {
    const [files, setFiles] = useState([]);
    const {register, handleSubmit} = useForm();
    const [openedFile, setOpenedFile] = useState();
    const [openedPdf, setOpenedPdf] = useState(new Blob());
    const [allUsers, setAllUsers] = useState([]);

    useEffect(() => {
        getAllFileInfos().then(response => {
            response.json().then(data => setFiles(data));
        })
        getAllUsers().then(response => {
            response.json().then(data => setAllUsers(data));
        })
    }, [false])
    const updateFiles = (data) => {
        if (data.userId) {
            getAllFileInfosByUser(data.userId).then(response => {
                response.json().then(data => setFiles(data));
            })
        } else {
            getAllFileInfos().then(response => {
                response.json().then(data => setFiles(data));
            })
        }
    }

    const openFile = (file) => {
        fetch(`${HOST_ADDRESS}api/files/from-FS/${file.id}`, {
            method: "GET",
            headers: {
                'Access-Control-Allow-Origin': '*',
                'Authorization': 'Bearer_' + getTokenFromLocalStorage()
            }
        }).then(response => response.blob())
            .then(blob => {
                setOpenedFile(file);
                setOpenedPdf(blob);
            });
    }
    return (
        <div>
            <Typography style={{textAlign:"center"}} variant={'h6'}>Hello, {getUsername()}!
            </Typography>
            <div style={{float: 'left', display: 'inline-block', width: '35%'}}>
                <label>
                    Set user
                    <form onSubmit={handleSubmit(updateFiles)}>
                        <select name={"userId"} placeholder={"User id"} defaultValue={""} ref={register}>
                            <option value={""}>All</option>

                            ${allUsers && allUsers.map(user => <option key={user.id}
                                                                       value={user.id}>{user.username}</option>)}
                        </select>
                        <input type={"submit"} value={"Filter"}/>
                    </form>
                </label>

                <br/>
                <br/>
                <br/>
                <label>Upload file for user
                    <UploadFile/>
                </label>
                <br/>
                <table style={{width: "50%"}}>
                    <thead>
                    <th>File Name</th>
                    <th>upload date</th>
                    <th>update date</th>
                    <th>user</th>
                    <th>Delete</th>
                    <th>View</th>
                    </thead>
                    <tbody>
                    {files.map(file =>
                        <tr>
                            <td>{file.fileName}</td>
                            <td>{getDateTimeFromTimeStamp(file.uploadTimestamp)}</td>
                            <td>{getDateTimeFromTimeStamp(file.updateTimestamp)}</td>
                            <td>
                                <UsernameById id={file.userId}/>
                            </td>
                            <td>
                                <button onClick={() => {
                                    deleteFile(file.id).then(r => {
                                        if (r.status === 200) {
                                            window.location.reload(false);
                                        } else {
                                            r.json().then(data =>
                                                alert("error in deleting file.\n Message: " + data.message));
                                        }
                                    })
                                }} value={"delete"}>delete
                                </button>
                            </td>
                            <td><PictureAsPdfIcon onClick={() => openFile(file)}/></td>
                        </tr>
                    )}
                    </tbody>
                </table>
            </div>
            { openedFile && openedPdf && <div style={{display: 'inline-block'}}>
                <PdfReader file={openedFile} pdf={openedPdf}/>
            </div>}
        </div>
    );
}

function UploadFile() {
    const {register, handleSubmit} = useForm();
    const [file, setFile] = useState();

    const onSubmit = data => {
        const formData = new FormData();
        formData.append('file', file);
        formData.append('userId', data.userId);
        formData.append('description', data.description);

        fetch(`${HOST_ADDRESS}api/files/upload-file`, {
            method: 'POST',
            headers: {
                'Access-Control-Allow-Origin': '*',
                'Authorization': 'Bearer_' + getTokenFromLocalStorage()
            },

            mode: 'cors',
            body: formData
        }).then(r => {
            if (r.status === 200) {

                window.location.reload(false);
            }
        })
    }

    const onFileChangeHandler = (e) => {
        setFile(e.target.files[0]);
    }

    return (
        <div>
            <form onSubmit={handleSubmit(onSubmit)}>
                <label>
                    UserId:
                    <input type={"text"} name={"userId"} ref={register}/>
                </label>
                <br/>
                <label>
                    Description:
                    <input type={"text"} name={"description"} ref={register}/>
                </label>
                <br/>
                <input type={"file"} multiple={false}
                       onChange={onFileChangeHandler} name={"file"} accept={"application/pdf"}/>
                <input type={'submit'} value={"Upload."}/>
            </form>
        </div>
    );
}

export function PdfReader(props) {
    const fileInfo = props.file;
    const pdf = props.pdf;
    const [numPages, setNumPages] = useState();
    const onDocumentLoadSuccess = ({numPages}) => {
        setNumPages(numPages);
    };

    return (
        <div>
            <Typography variant={'h6'} color={'primary'}>
                {fileInfo?.fileName}
            </Typography>
            <br/>
            <Typography variant={'body'} color={'primary'}>
               Description: {fileInfo?.description}
            </Typography>
            <br/>
            <Paper style={{maxHeight: "750px", overflow: 'auto', width: 'inherit'}}>
                <Document style={{height: '700', width: 'inherit'}} file={blobToFile(pdf, fileInfo?.fileName)}
                          onLoadSuccess={onDocumentLoadSuccess}>
                    {Array.apply(null, Array(numPages))
                        .map((x, i) => i + 1)
                        .map(page => <Page pageNumber={page}/>)}

                </Document>
            </Paper>
        </div>
    )

}

function blobToFile(theBlob, fileName) {
    //A Blob() is almost a File() - it's just missing the two properties below which we will add
    theBlob.lastModifiedDate = new Date();
    theBlob.name = fileName;
    return theBlob;
}