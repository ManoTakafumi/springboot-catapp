const frames = [
            "/images/cat_1.png",
            "/images/cat_2.png",
            "/images/cat_3.png",
            "/images/cat_4.png"
        ];

        let index = 0;

        setInterval(() => {
            index = (index + 1) % frames.length;
            document.getElementById("cat").src = frames[index];
        }, 500);