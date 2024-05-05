import React, { useState } from "react";
import { Form, Input, Button, Card, Typography } from 'antd';
import { UserOutlined } from '@ant-design/icons';


export default function AddUser() {
    const [user, setUser] = useState({
        msisdn: '',
        tariffId: 0,
        money: 100.0,
        name: '',
        surname: '',
        patronymic: ''
    });

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

    function tariffIdValidator(rules, value) {
        if (value == 11 || value == 12) {
            return Promise.resolve;
        } else {
            return Promise.reject('Tariff id should be 11 or 12');
        }
    }

    const submit = (e) => {
        e.preventDefault();
        const requestOptions = {
            method: 'POST',
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Basic YWRtaW46YWRtaW4="
            },
            body: JSON.stringify({
                msisdn: user.msisdn,
                tariffId: user.tariffId,
                money: user.money,
                name: user.name,
                surname: user.surname,
                patronymic: user.patronymic
            }),
        };
        console.log(requestOptions);
        fetch("http://localhost:2004/admin/save", requestOptions)
        .then((response) => {
            if (response.ok) {
                console.log('Success');
                alert('Success')
            } else {
                console.log('Error');
                alert('Error')
            }
        })
    }

    return (
        <div>
            <h2>Here you can add new user</h2>
            <Card title='Add new user' style={{ borderRadius: '10px' }}>
                <Form layout="vertical">
                    <Form.Item
                        name='msisdn'
                        label='Phone number'
                        onChange={ (e) => handle(e) }
                        id='msisdn'
                        value={ user.msisdn }
                        rules={[{
                            required: true,
                            message: 'Please input user phone number',
                        },
                            { validator: phoneNumberValidator }
                        ]}
                    >
                        <Input
                            type="number"
                            placeholder='User phone number'
                            prefix={ <UserOutlined className='site-form-item-icon' /> }
                        />
                    </Form.Item>
                    <Form.Item
                        name='tariffId'
                        label='Tariff id'
                        onChange={ (e) => handleNum(e) }
                        id='tariffId'
                        value={ user.tariffId }
                        rules={[{
                            required: true,
                            message: "Please input user's tariff id",
                        },
                            { validator: tariffIdValidator }
                        ]}
                    >
                        <Input
                        type="number"
                            placeholder="Tariff id (11 or 12)"
                        />
                    </Form.Item>
                    <Form.Item
                        name='money'
                        label='Money'
                        onChange={ (e) => handleNum(e) }
                        id='money'
                        value={ user.money }
                    >
                        <Input
                            placeholder="Money (100 by default)"
                        />
                    </Form.Item>
                    <Form.Item
                        name='name'
                        label="User's name"
                        onChange={ (e) => handle(e) }
                        id='name'
                        value={ user.name }
                        rules={[{
                            required: true,
                            message: "Please input user's name",
                        },
                        ]}
                    >
                        <Input
                            placeholder="Name"
                        />
                    </Form.Item>
                    <Form.Item
                        name='surname'
                        label="User's surname"
                        onChange={ (e) => handle(e) }
                        id='surname'
                        value={ user.surname }
                        rules={[{
                            required: true,
                            message: "Please input user's surname",
                        },
                        ]}
                    >
                        <Input
                            placeholder="Surname"
                        />
                    </Form.Item>
                    <Form.Item
                        name='patronymic'
                        label="User's patronymic"
                        onChange={ (e) => handle(e) }
                        id='patronymic'
                        value={ user.patronymic }
                        rules={[{
                            required: true,
                            message: "Please input user's patronymic",
                        },
                        ]}
                    >
                        <Input
                            placeholder="Patronymic"
                        />
                    </Form.Item>
                </Form>
                <Button
                    type="primary"
                    onClick={ (e) => submit(e) }
                >
                    Save
                </Button>
            </Card>
            <Button
                href="/admin"
            >
                Go Back
            </Button>
        </div>
        
    )
}