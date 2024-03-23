import {Link, useHistory} from "react-router-dom";
import React from "react";
import Toolbar from "@material-ui/core/Toolbar";
import Typography from "@material-ui/core/Typography";
import Button from "@material-ui/core/Button";
import AppBar from "@material-ui/core/AppBar";
import {makeStyles} from '@material-ui/core/styles';
import {isAuthenticated, isEmployee, isManager} from "../auth/Auth";
import {LogoutButton} from "../logout/LogoutButton";

const useStyles = makeStyles((theme) => ({
    root: {
        flexGrow: 1,
    },
    menuButton: {
        marginRight: theme.spacing(2),
    },
    title: {
        flexGrow: 0.2,
        color: 'white'
    },
}));

export function Header() {
    const classes = useStyles();
    const history = useHistory();
    return (
        <div>
            <AppBar position="static">
                <Toolbar>
                    <div style={{display:"inline-block"}}>
                    {!isAuthenticated() && <div className={classes.title}>
                        <Link to='/register'>
                            <Typography variant="h6" className={classes.title}>
                                Registration
                            </Typography>
                        </Link>
                    </div>}

                    {isManager() &&
                    <div className={classes.title}>
                        <Link to='/manager'> <Typography variant="h6" className={classes.title}>
                            Manager page
                        </Typography>
                        </Link>
                    </div>
                    }
                    {isEmployee() &&
                    <div className={classes.title}>
                        <Link to='/employee'><Typography variant="h6" className={classes.title}>
                            Employee page
                        </Typography>
                        </Link>
                    </div>
                    }
                    </div>
                    <div style={{float:'right',marginLeft:'auto',marginRight:-12}}>
                    {isAuthenticated() ? <LogoutButton/>
                        : <Button color="inherit" style={{backgroundColor: 'green'}}
                                  onClick={() => history.push("/login")}>Login</Button>}
                    </div>

                </Toolbar>
            </AppBar>
        </div>

    )
}
