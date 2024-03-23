import jwt from 'jwt-decode'

const MANAGER_ROLE = 'MANAGER';
const EMPLOYEE_ROLE = 'EMPLOYEE';

export function saveAuthentication(stringToken) {
    saveUserInLocalStorage(stringToken);
}

export function removeAuthentication() {
    window.localStorage.removeItem('token');
}


function getUserFromToken() {
    const token = getTokenFromLocalStorage();
    if (token) {
        const jwtObject = jwt(token);
        return JSON.parse(jwtObject.sub);
    } else return null;
}


export function getUserId() {
    return getUserFromToken()?.id;
}

export function getUsername() {
    return getUserFromToken()?.username;
}

export function getRole() {
    return getUserFromToken()?.role;
}

export function isManager() {
    console.log(getRole());
    console.log(getRole() === MANAGER_ROLE);
    return getRole() === MANAGER_ROLE;
}

export function getHeaders() {
    return {
        'Content-Type': 'application/json',
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Bearer_' + getTokenFromLocalStorage()
    }
}

export function isEmployee() {
    return getRole() === EMPLOYEE_ROLE;
}

export function isAuthenticated() {
    return getUserId() && getUsername() && getRole();
}


function saveUserInLocalStorage(token) {
    window.localStorage.setItem('token', token);
}

export function getTokenFromLocalStorage() {
    return window.localStorage.getItem('token');
}