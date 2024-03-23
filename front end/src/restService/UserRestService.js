import {HOST_ADDRESS} from "./consts";
import {getHeaders} from "../auth/Auth";

export async function getUserById(id) {
    return await fetch(`${HOST_ADDRESS}api/users/${id}`, {
        method: "GET",
        headers: getHeaders()
    });
}
export async function getAllUsers() {
    return await fetch(`${HOST_ADDRESS}api/users/`, {
        method: "GET",
        headers: getHeaders()
    });
}

export async function sendLoginRequest(data) {
    return await fetch(`${HOST_ADDRESS}api/auth/login`, {
        method: "POST",
        headers: {
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin': '*',
        },
        body: JSON.stringify(data)
    });
}

export async function sendRegistrationRequest(data) {
    return await fetch(`${HOST_ADDRESS}api/auth/register`, {
        method: "POST",
        headers: {
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin': '*',
        },
        body: JSON.stringify(data)
    });
}