import { Button, Flex } from "antd";
import React from "react";
// import "./../../App.css";


export default function Admin() {
    return (
        <div>
            <h1>Hello admin</h1>
            <Flex>
                <Button
                    type="primary"
                    href="/admin/save"
                >
                    Add new user
                </Button>
                <Button
                    href="/"
                >
                    Go Home
                </Button>
                <Button
                    type="primary"
                    href="/admin/change-tariff"
                >
                    Change user's tariff
                </Button>
            </Flex>
        </div>
        
    )
}