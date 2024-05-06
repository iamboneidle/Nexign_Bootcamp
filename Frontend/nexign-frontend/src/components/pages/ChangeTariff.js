import React, { useState } from "react";
import { Form, Input, Button, Card} from 'antd';
import { UserOutlined } from '@ant-design/icons';


export default function ChangeTariff() {
    const [user, setUser] = useState({
        msisdn: '',
        tariffId: 0
    });

    const handle = (e) => {
        const newUser = { ...user };
        newUser[e.target.id] = e.target.value;
        setUser(newUser);
    }

    const handleNum = (e) => {
        const newUser = { ...user };
        newUser[e.target.id] = +e.target.value;
        setUser(newUser);
    }

    function phoneNumberValidator(rules, value) {
        const phoneNumberRegex = /^7\d{10}$/;
        if (value && !value.match(phoneNumberRegex)) {
            return Promise.reject('Phone number should be 7XXXXXXXXXX');
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
        e.preventDefault()
        const requestOptions = {
            method: 'POST',
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Basic YWRtaW46YWRtaW4="
            },
            body: JSON.stringify({
                msisdn: user.msisdn,
                tariffId: user.tariffId,
            }),
        };
        console.log(requestOptions);
        fetch("http://localhost:2004/admin/change-tariff", requestOptions)
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
            <h2>Here yo can change user's tariff</h2>
            <Card title="Change user's tariff" style={{ borderRadius: '10px' }}>
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
                            placeholder="Tariff id (11 or 12)"
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
                href="/admin"
            >
                Go Back
            </Button>
        </div>
    )
}