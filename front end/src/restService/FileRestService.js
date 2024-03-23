import {HOST_ADDRESS} from "./consts";
import {getHeaders} from "../auth/Auth";

export async function getAllFileInfos() {
    return await fetch(`${HOST_ADDRESS}api/files/`, {
        method: "GET",
        headers: getHeaders()
    });
}

export async function getAllFileInfosByUser(id) {
    return await fetch(`${HOST_ADDRESS}api/files/?userId=${id}`, {
        method: "GET",
        headers: getHeaders()
    });
}

export async function deleteFile(id) {
    return await fetch(`${HOST_ADDRESS}api/files/delete/${id}`, {
        method: "DELETE",
        headers: getHeaders()
    });
}
