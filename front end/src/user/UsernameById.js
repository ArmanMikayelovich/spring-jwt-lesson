import React, {useEffect, useState} from "react";
import {getUserById} from "../restService/UserRestService";

export function UsernameById(props) {
    const id = props.id;
    const [username, setUsername] = useState();

    useEffect(() => {
        getUserById(id).then(response => {
            response.json().then(data => setUsername(data.username));
        })
    })

    return (
        <div>
            {username}
        </div>
    )

}