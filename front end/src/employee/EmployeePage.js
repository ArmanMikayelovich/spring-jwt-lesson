import React, {useEffect, useState} from "react";
import {useForm} from "react-hook-form";
import {deleteFile, getAllFileInfos, getAllFileInfosByUser} from "../restService/FileRestService";
import {UsernameById} from "../user/UsernameById";
import {HOST_ADDRESS} from "../restService/consts";
import {getTokenFromLocalStorage, getUserId, getUsername} from "../auth/Auth";
import {Document, Page, pdfjs} from "react-pdf";
import Typography from "@material-ui/core/Typography";
import PictureAsPdfIcon from '@material-ui/icons/PictureAsPdf';
import Paper from "@material-ui/core/Paper";
import {getAllUsers} from "../restService/UserRestService";
import {PdfReader} from "../manager/ManagerPage";


pdfjs.GlobalWorkerOptions.workerSrc = `//cdnjs.cloudflare.com/ajax/libs/pdf.js/${pdfjs.version}/pdf.worker.js`;

function getDateTimeFromTimeStamp(time) {
    return `${time.year}/${time.monthValue}/${time.dayOfMonth} ${time.hour} : ${time.minute} : ${time.minute}`
}


export function EmployeePage() {
    const [files, setFiles] = useState([]);
    const [openedFile, setOpenedFile] = useState();
    const [openedPdf, setOpenedPdf] = useState(new Blob());
    const userId = getUserId();

    useEffect(() => {
        getAllFileInfosByUser(userId).then(response => {
            response.json().then(data => setFiles(data));
        })
    }, [userId])


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
            <div style={{float: 'left', display: 'inline-block', width: '35%'}}>
                <Typography variant={'h6'}>Hello, {getUsername()}!
                </Typography>
                {files.length !==0 ?<table style={{width: "50%"}}>
                    <thead>
                    <th>File Name</th>
                    <th>upload date</th>
                    <th>update date</th>
                    <th>View</th>
                    </thead>
                    <tbody>
                    {files.map(file =>
                        <tr>
                            <td>{file.fileName}</td>
                            <td>{getDateTimeFromTimeStamp(file.uploadTimestamp)}</td>
                            <td>{getDateTimeFromTimeStamp(file.updateTimestamp)}</td>
                            <td><PictureAsPdfIcon onClick={() => openFile(file)}/></td>
                        </tr>
                    )}
                    </tbody>
                </table> : <Typography variant={'h6'}>You do not have any pdf file 😎</Typography> }
            </div>
            { openedFile && openedPdf && <div style={{display: 'inline-block'}}>
                <PdfReader file={openedFile} pdf={openedPdf}/>
            </div>}
        </div>
    );
}

