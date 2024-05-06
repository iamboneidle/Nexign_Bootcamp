import { Button } from "antd";
import React from "react";


export default function User() {
    return (
        <div>
        <h1>Hello user</h1>
        <Button
            type="primary"
            href="/user/put-money"
        >
            Put money
        </Button>
        <Button
            href="/"
        >
            Go Home
        </Button>
        </div>
        
    )
}