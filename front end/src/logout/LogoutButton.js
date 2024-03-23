import * as React from 'react';
import {useHistory} from 'react-router-dom';
import {removeAuthentication} from "../auth/Auth";
import Button from "@material-ui/core/Button";
export function LogoutButton() {
    const history = useHistory();

    const logout= () => {
        removeAuthentication();
        window.location.reload();
    }

    return (
        <div>
            <Button variant={'contained'} color={'secondary'} onClick={logout}>Log out</Button>
        </div>
    )

}