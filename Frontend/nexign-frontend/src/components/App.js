import './App.css';
import React from 'react';

import {
    BrowserRouter,
    Routes,
    Route,
} from "react-router-dom";
import Home from './pages/Home';
import User from './pages/User';
import Admin from './pages/Admin';
import AddUser from './pages/AddUser';
import ChangeTariff from './pages/ChangeTariff';
import PutMoney from './pages/PutMoney';

function App() {
    return (
        <div className="App">
            <header className="App-header">
                <BrowserRouter>
                    <Routes>
                        <Route path='/admin' element={<Admin />} />
                        <Route path='/user' element={<User />} />
                        <Route path='/' element={<Home />} />
                        <Route path='/admin/save' element={<AddUser />} />
                        <Route path='/admin/change-tariff' element={<ChangeTariff />} />
                        <Route path='/user/put-money' element={<PutMoney />} />
                    </Routes>                
                </BrowserRouter>
            </header>
        </div>
    );
}

export default App;




