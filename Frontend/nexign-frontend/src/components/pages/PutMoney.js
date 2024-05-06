import React, { useState } from "react";
import { Form, Input, Button, Card } from 'antd';
import { UserOutlined } from '@ant-design/icons';


export default function PutMoney() {
    const [user, setUser] = useState({
        msisdn: '',
        money: 0
    });

    const password = 'user';

    const handle = (e) => {
        const newUser = { ...user };
        newUser[e.target.id] = e.target.value;
        setUser(newUser);
    }

    const handleNum = (e) => {
        const newUser = { ... user };
        newUser[e.target.id] = +e.target.value;
        setUser(newUser);
    }

    function phoneNumberValidator(rules, value) {
        const phoneNumberRegex = /^7\d{10}$/;
        if (value && !value.match(phoneNumberRegex)) {
            return Promise.reject('number should be 7XXXXXXXXXX');
        } else {
            return Promise.resolve();
        }
    }

    function basicEncoder(name, password) {
        return 'Basic ' + btoa(name + ':' + password)
    }

    const submit = (e) => {
        e.preventDefault();
        const requestOptions = {
            method: 'POST',
            headers: {
                "Content-Type": "application/json",
                "Authorization": basicEncoder(user.msisdn, password)
            },
            body: JSON.stringify({
                msisdn: user.msisdn,
                money: user.money,
            }),
        };
        fetch("http://localhost:2004/user/put-money", requestOptions)
        .then((response) => {
            if (response.ok) {
                console.log('Success');
                alert('Success')
            } else {
                console.log('Error');
                alert('Error')
            }
        })
    }; 

    return (
        <div>
            <h2>Here you can put money</h2>
            <Card title="Put money" style={{ borderRadius: '10px' }}>
                <Form layout="vertical">
                    <Form.Item
                        name='msisdn'
                        label='Phone number'
                        onChange={ (e) => handle(e) }
                        id='msisdn'
                        value={ user.msisdn }
                        rules={[{
                            required: true,
                            message: 'Please input your phone number',
                        },
                            { validator: phoneNumberValidator },
                        ]}
                    >
                        <Input
                            type="number"
                            placeholder='Your phone number'
                            prefix={ <UserOutlined className='site-form-item-icon' /> }
                        />
                    </Form.Item>
                    <Form.Item
                        name='money'
                        label='Money'
                        onChange={ (e) => handleNum(e) }
                        id='money'
                        value={ user.money }
                        rules={[{
                            required: true,
                            message: "Please input amount of money you want to put",
                        },
                        ]}
                    >
                        <Input
                            placeholder="Amount of money"
                        />
                    </Form.Item>
                </Form>
                <Button
                    type="primary"
                    onClick={ (e) => submit(e) }
                >
                    Submit
                </Button>
            </Card>
            <Button
                href="/user"
            >
                Go Back
            </Button>
        </div>
    )
}