import React, {useState} from "react";
import {useForm} from "react-hook-form";
import TextField from "@material-ui/core/TextField";
import {useHistory} from 'react-router-dom'
import Button from "@material-ui/core/Button";
import {saveAuthentication} from "../auth/Auth";
import {sendRegistrationRequest} from "../restService/UserRestService";

export function RegisterPage() {
    const history = useHistory();
    const {register, handleSubmit} = useForm();
    const [errorMessage, setErrorMessage] = useState('');
    const onSubmit = data => {
        sendRegistrationRequest(data).then(r => {
            if (r.status !== 200) {
                r.json().then(apiError => {
                    setErrorMessage(apiError.message)
                })
            } else if (r.status === 200) {
                r.json().then(jwt => {
                    saveAuthentication(jwt.token)
                })
                history.push('/');
            }
        })
    }
    return (
        <div>
            <h3>Registration</h3>
            <form onSubmit={handleSubmit(onSubmit)}>
                {errorMessage && <p style={{color: 'red'}}>{errorMessage}</p>}
                <p>
                    <TextField inputRef={register} variant={'outlined'} style={{width: '15%'}} name={'username'}
                               id="standard-basic" label="Username"/>
                </p>

                <p>
                    <TextField type={'password'} inputRef={register} style={{width: '15%'}} name={'password'}
                               variant={'outlined'}
                               id="standard-basic" label="Password"/>
                </p>

                <Button variant={"contained"} style={{backgroundColor: 'green'}} type={'submit'}
                        color={'primary'}> Sign up</Button>
            </form>
        </div>
    )
}