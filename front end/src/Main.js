import {Redirect, Route, Switch} from "react-router-dom";
import React from "react";
import {ManagerPage} from "./manager/ManagerPage";
import {LoginPage} from "./login/LoginPage";
import {isAuthenticated, isEmployee, isManager} from "./auth/Auth";
import {RegisterPage} from "./register/RegisterPage";
import {EmployeePage} from "./employee/EmployeePage";

export function Main() {

    return (

        <Switch>
            <Route exact={true} path='/'>
                {isManager() ? <Redirect to={"/manager"}/> :
                    isEmployee() ? <Redirect to={"/employee"}/> : <Redirect to={"/login"}/>}
            </Route>

            <Route exact={true} path='/register'>
                {isAuthenticated() ? <Redirect to={"/"}/> : <RegisterPage/>}
            </Route>

            <Route exact={true} path='/manager'>
                {isManager() ? <ManagerPage/> : <Redirect to={"/"}/>}
            </Route>

            <Route exact={true} path='/employee'>
                {isEmployee() ? <EmployeePage/> : <Redirect to={"/"}/>}
            </Route>

            <Route exact={true} path='/login'>
                {isAuthenticated() ? <Redirect to={"/"}/> : <LoginPage/>}
            </Route>

        </Switch>


    );
}