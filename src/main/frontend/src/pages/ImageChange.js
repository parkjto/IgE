import React, { useState, useEffect } from "react";
import Egg from "../img/Egg.png";
import FaceSa from "../img/Face Savoring Food.png";
import Milk from "../img/Milk.png";
import Pizza from "../img/Pizza.png";
import Ramen from "../img/Ramen.png";
import Sandwich from "../img/Sandwich.png";
import Sousa from "../img/Sausages.png";
import styles from "./ImageChange.module.css";



const images = [Egg, FaceSa, Milk, Pizza,Ramen,Sandwich,Sousa];

function ImageSlider() {
    const [currentIndex, setCurrentIndex] = useState(0);

    useEffect(() => {
        const interval = setInterval(() => {
            setCurrentIndex((prevIndex) => (prevIndex + 1) % images.length);
        }, 280);

        return () => clearInterval(interval);
    }, []);

    return (
        <div>
            <img src={images[currentIndex]} alt="슬라이드 이미지" className={styles.slideimage} />
        </div>
    );
}

export default ImageSlider;
