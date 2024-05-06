import React from "react";
import { Button, Flex } from 'antd';


export default function Home() {
    return (
        <div>
            <h1>Hello at home</h1>
            <Flex>
                <Button 
                    type="primary"
                    href="/user"
                >
                    I am user
                </Button>
                <Button 
                    type="link"
                    href="http://localhost:2004/swagger-ui/index.html"
                >
                    Go to Swagger-UI
                </Button>
                <Button 
                    danger
                    href="/admin"
                >
                    I am admin
                </Button>
            </Flex>
            
        
        </div>
    )
}