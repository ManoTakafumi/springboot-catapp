function feedCat(catId) {
            fetch('/cats/' + catId + '/feed', {
                method: 'POST'
            })
            .then(response => response.json())
            .then(data => {

                if (data.message) {
                    alert(data.message);
                }

                //満腹度更新
                document.getElementById("hungerValue_" + catId).innerText = data.hunger;
                document.getElementById("energyValue_" + catId).innerText = data.energy;
                document.getElementById("levelValue_" + catId).innerText = data.level;
                document.getElementById("staminaValue_" + catId).innerText = data.stamina;

                if (data.bonusMessage) {
                    alert(data.bonusMessage);
                }

                //画像更新
                if (data.imageStatus === "eating") {
                    document.getElementById("catImage_" + catId).src = "/images/eat.png";
                    document.getElementById("catStatusText_" + catId).innerText = "ごはんを食べています";
                }

                setTimeout(() => {
                    document.getElementById("catImage_" + catId).src = "/images/wait.png";
                    document.getElementById("catStatusText_" + catId).innerText = "まったりしています";
                }, 2500);

                if (data.actionBlocked) {
                    alert(data.message);

                    document.getElementById("catStatusText_" + catId).innerText = "疲れて動けない_";
                    return;
                }
            })
            .catch(error => {
                console.error('Error:', error);
            });
        }

function playCat(catId) {
    fetch('/cats/' + catId + '/play', {
        method: 'POST'
    })
        .then(response => response.json())
        .then(data => {

              if (data.message) {
                    alert(data.message);
              }

              //元気度更新
              document.getElementById("hungerValue_" + catId).innerText = data.hunger;
              document.getElementById("energyValue_" + catId).innerText = data.energy;
              document.getElementById("levelValue_" + catId).innerText = data.level;
              document.getElementById("staminaValue_" + catId).innerText = data.stamina;

              if (data.bonusMessage) {
                  alert(data.bonusMessage);
              }

              if (data.imageStatus === "playing") {
                  document.getElementById("catImage_" + catId).src = "/images/play.png";
                  document.getElementById("catStatusText_" + catId).innerText = "元気に遊んでいます";
              }

              setTimeout(() => {
                  document.getElementById("catImage_" + catId).src = "/images/wait.png";
                  document.getElementById("catStatusText_" + catId).innerText = "まったりしています";
              }, 2500);

              if (data.actionBlocked) {
                  alert(data.message);
                  document.getElementById("catStatusText_" + catId).innerText = "疲れて動けない_";

                  return;
              }

        });
    }

function sleepCat(catId) {
    fetch('/cats/' + catId + '/sleep', {
        method: 'POST'
    })
        .then(response => response.json())
        .then(data => {

              document.getElementById("staminaValue_" + catId).innerText = data.stamina;

              if (data.imageStatus === "sleeping") {
                  document.getElementById("catImage_" + catId).src = "/images/sleep.png";
                  document.getElementById("catStatusText_" + catId).innerText = "ぐっすり寝ています";
              }

              if (data.bonusMessage) {
                  alert(data.bonusMessage);
              }

              setTimeout(() => {
                  document.getElementById("catImage_" + catId).src = "/images/wait.png";
                  document.getElementById("catStatusText_" + catId).innerText = "まったりしています";
              }, 2000);

        });
    }

setInterval(() => {

    document.querySelectorAll("[id^='hungerValue_']")
        .forEach(element => {

            const catId =
                element.id.replace("hungerValue_", "");

            fetch('/cats/' + catId + '/status')

                .then(response => response.json())

                .then(data => {

                    document.getElementById("hungerValue_" + catId)
                        .innerText = data.hunger;

                    document.getElementById("energyValue_" + catId)
                        .innerText = data.energy;

                    document.getElementById("staminaValue_" + catId)
                        .innerText = data.stamina;

                    document.getElementById("levelValue_" + catId)
                        .innerText = data.level;

                    if (data.condition === "hungry") {

                         document.getElementById("catImage_" + catId).src = "/images/hungry.png";

                         document.getElementById("catStatusText_" + catId).innerText = "おなかペコペコ…";
                    }

                    else if (data.condition === "tired") {

                         document.getElementById("catImage_" + catId).src = "/images/tired.png";

                         document.getElementById("catStatusText_" + catId).innerText = "ぐったりしている…";
                    }

                    else {

                        document.getElementById("catImage_" + catId).src = "/images/wait.png";

                        document.getElementById("catStatusText_" + catId).innerText = "まったりしています";
                    }
                });
        });


}, 10000);