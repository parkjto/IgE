import React, { Component } from 'react';
import style from './RandomMenu.module.css';
import RandomInform from "./RandomInform";

class RandomMenu extends Component {
    constructor(props) {
        super(props);
        this.state = {
            // showImageChange: false,
            textVisible: true,
            showRandomInform : false,
        };
    }

    handleClick = () => {
        this.setState({
            // text: "추천 메뉴가 변경되었습니다! 🍕🍔",
            // showImageChange: true,
            textVisible: false,
            showRandomInform : true,
        });

        // setTimeout(() => {
        //     this.setState({
        //         textVisible: false,
        //         // showImageChange: false,
        //         showRandomInform : true
        //     });
        // }, 3000);
    };

    render() {
        return (
            <div className={style.container} onClick={this.handleClick} style={{ cursor: 'pointer' }}>
                {this.state.textVisible && <p className={style.text}>메뉴 추천 해드릴 게요 😘🧐</p>}
                {/*{this.state.showImageChange && <ImageChange/>}*/}
                {this.state.showRandomInform && <RandomInform/>}
            </div>
        );
    }
}

export default RandomMenu;
