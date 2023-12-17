function navigateToMessageActivity() {
    Android.navigateMessage();
}
function sendNotification() {
    Android.showNotification();
}
const estUnOrdinateur = () => {
    const userAgent = navigator.userAgent.toLowerCase();
    const isAndroid = userAgent.indexOf("android") > -1;
    const isMobile = /(iphone|ipad|ipod|android)/i.test(userAgent);

    return !(isAndroid || isMobile);
};
console.log("ecran " + estUnOrdinateur());
var ordi = estUnOrdinateur();
function affichageMobile() {
    if (ordi) {
        let Message = document.getElementById('Message');
        Message.style.display = 'none';
    }
}
affichageMobile();
var adresse = "https://us-central1-boulou-functions-for-devs.cloudfunctions.net/";
var deviceId = "bf02db56c7a8be675baaey";
var devId = "-Nlm4vKk3psfiVmtLOhE";
var mail = "jrmanouhoseah@gmail.com";
var etatAction = adresse + "boulou_switch_device";
var etatActuel = "https://us-central1-boulou-functions-for-devs.cloudfunctions.net/boulou_check_deviceStatus?developerId=" + devId + "&email=" + mail + "&deviceId=" + deviceId;
var on = "ON";
var off = "OFF";
var ip = "http://192.168.1.149:8080"
var urlSever = "/iot/scene/creer?"

// horaire scene serie= temps=14:00:00 type=scene action=on/off
// timer scene  serie= temps=14:00:00 type=timer action=on/off

function etat(action) {
    const data = {
        developerId: devId,
        email: mail,
        deviceId: deviceId,
        switch_status: action
    };
    const requestOptions = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(data)
    };
    fetch(etatAction, requestOptions)
        .then(response => response.json())
        .then(data => console.log('Response:', data))
        .catch(error => console.error('Error:', error));
}
var previousState = null; // Variable pour stocker l'état précédent
function getData() {
    var checkbox = document.getElementById('switchToggle');
    fetch(etatActuel)
        .then(response => response.json())
        .then(itema => {
            var success = itema.success;
            if (success == false) {
                alert("Un problème est survenu lors de la récupération de la liste");
            } else {
                var currentState = itema.result.status.switch;
                var online = itema.result.online;

                if (online == true) {
                    var wifi = document.getElementById("online");
                    wifi.classList = "bi bi-wifi";
                }
                else {
                    var wifi = document.getElementById("online");
                    wifi.classList = "bi bi-wifi-off";
                }

                if (currentState !== previousState) {
                    if (currentState == true) {
                        checkbox.checked = true;
                    } else {
                        checkbox.checked = false;
                    }
                    console.log(currentState);
                    previousState = currentState; // Mettre à jour l'état précédent
                }
            }
        })
        .catch(error => {
            console.error('Une erreur s\'est produite:', error);
        });
}

setInterval(getData, 4000);

getData();


function switchButton() {
    var check = document.getElementById('switchToggle').checked;
    if (check == false) {
        etat(off);
    } else {
        etat(on);
    }
}

function sceneHoraire() {
    let heure = parseInt(document.getElementById('heureH').value);
    let minute = parseInt(document.getElementById('minuteH').value);
    let seconde = parseInt(document.getElementById('secondeH').value);
    let action = document.getElementById('actionH').value;
    if (heure < 10) {
        heure = "0" + heure;
    }
    if (minute < 10) {
        minute = "0" + minute;
    }
    if (seconde < 10) {
        seconde = "0" + seconde;
    }
    AjoutScene("scene", deviceId, heure, minute, seconde, action);
}
function sceneTimer() {
    let heure = parseInt(document.getElementById('heureT').value);
    let minute = parseInt(document.getElementById('minuteT').value);
    let seconde = parseInt(document.getElementById('secondeT').value);
    let action = document.getElementById('actionT').value;

    let maintenant = new Date();
    let heurePc = maintenant.getHours();
    let minutesPC = maintenant.getMinutes();
    let secondesPC = maintenant.getSeconds();

    console.log(`Il est ${heurePc} heures, ${minutesPC} minutes et ${secondesPC} secondes.`);

    const secondesToAdd = 5;
    let totalSecondes = (heurePc + heure) * 3600 + (minutesPC + minute) * 60 + (secondesPC + secondesToAdd + seconde);
    console.log(secondesPC, " ", secondesToAdd)

    heurePc = Math.floor(totalSecondes / 3600);
    totalSecondes %= 3600;
    minutesPC = Math.floor(totalSecondes / 60);
    secondesPC = totalSecondes % 60;

    if (heurePc < 10) {
        heurePc = `0${heurePc}`;
    }
    if (minutesPC < 10) {
        minutesPC = `0${minutesPC}`;
    }
    if (secondesPC < 10) {
        secondesPC = `0${secondesPC}`;
    }
    console.log(`Nouvelle heure : ${heurePc}:${minutesPC}:${secondesPC}`);

    AjoutScene("timer", deviceId, heurePc, minutesPC, secondesPC, action);
}

function AjoutScene(type, deviceId, idH, idM, idS, action) {
    var req = ip + urlSever + "temps=" + idH + ":" + idM + ":" + idS + "&serie=" + deviceId + "&action=" + action + "&type=" + type;
    console.log(req);
    fetch(req)
        .then(response => response.json())
        .then(data => {
            var res = data.resultat;
            if (res === "ko") {
                alert("Un problème est survenu lors de l'ajout");
            }
            else {
                alert("Ajout de scène réussi");
            }
        })
        .catch(error => {
            console.error('Une erreur s\'est produite:', error);
        });
}


function afficherContenu(sectionId) {
    var sections = document.querySelectorAll('[id$="Aff"]'); // Sélectionne tous les éléments dont l'ID se termine par "Aff"
    sections.forEach(function (section) {
        section.style.display = 'none'; // Cache toutes les sections
    });

    var sectionToShow = document.getElementById(sectionId);
    if (sectionToShow) {
        sectionToShow.style.display = 'block'; // Affiche la section correspondante
    }
}
function noneAfficherModal() {
    var sections = document.querySelectorAll('[id$="Aff"]'); // Sélectionne tous les éléments dont l'ID se termine par "Aff"
    sections.forEach(function (section) {
        section.style.display = 'none'; // Cache toutes les sections
    });
}

function listeScene() {
    var req = ip + "/iot/scene/lister?serie=bf02db56c7a8be675baaey";
    fetch(req)
        .then(response => response.json())
        .then(data => {
            var res = data.resultat;

            if (res === "ko") {
                alert("Un problème est survenu lors de la recuperation de la liste");
            } else {
                const tableau = document.getElementById("sceneTableau");

                data.forEach(scene => {
                    tableau.innerHTML += `
                    <tr>
                      <td>${scene.temps}</td>
                      <td>${scene.action}</td>
                      <td>${scene.type}</td>
                    </tr>
                  `;
                })
            }
        })
}

listeScene();

function setBatteryLevel(level, val) {
    level = Math.max(0, Math.min(100, level));
    var meterSpan = document.querySelector('.meter > span');
    meterSpan.style.width = level + '%';

    var h1Element = document.getElementById('battery-level');
    h1Element.textContent = "Seuil : " + level + '%  ' + "(" + val + 'KW)';
    h1Element.style.textAlign = "center";
    var meter = document.getElementById('battery-meter');
    if (level < 20) {
        meter.style.backgroundColor = 'green';
    } else if (level < 40) {
        meter.style.backgroundColor = 'limegreen';
    } else if (level < 60) {
        meter.style.backgroundColor = 'yellow';
    } else if (level < 80) {
        meter.style.backgroundColor = 'orange';
    } else {
        meter.style.backgroundColor = 'red';
    }
}


function seuilPost() {
    var kilo = document.getElementById('consom').value;
    console.log(kilo)
    var req = ip + "/iot/scene/consommation/creer?&serie=bf02db56c7a8be675baaey&action=off&maxKwh=" + kilo + "&maxA=0"
    fetch(req)
        .then(response => response.json())
        .then(data => {
            if (data.resultat == "ok") {
                alert('Seuil parametré');
            }
            else {
                console.log("Erreur");
            }
        })
}
function seuilGetKW() {

}

seuilGetKW();


function resltatKw() {
    var kilo = document.getElementById('consom').value;
    console.log(kilo)
    var req = ip + "/iot/scene/consommation/getMaxkwh"
    fetch(req)
        .then(response => response.json())
        .then(data => {
            var val = data.resultat;

            var rep = "https://us-central1-boulou-functions-for-devs.cloudfunctions.net/boulou_get_deviceStatistics?developerId=-Nlm4vKk3psfiVmtLOhE&email=jrmanouhoseah@gmail.com&deviceId=bf02db56c7a8be675baaey&period_type=year&period_value=2023"
            fetch(rep)
                .then(response => response.json())
                .then(data => {
                    if (data.success == true) {
                        var result = data.result;
                        const valeur202312 = result['202312'];
                        // console.log('Valeur de 202312 :', valeur202312);
                        var valInit = (valeur202312 * 100) / val;
                        var pourcentageArrondi = valInit.toFixed(2);
                        var pourcentage = parseFloat(pourcentageArrondi);

                        if (pourcentage >= 100) {
                            if (localStorage.getItem("notiff") == null) {
                                localStorage.setItem("notiff", "true");
                                sendNotification();
                            }
                            setBatteryLevel(100, valeur202312);
                        }
                        else if (pourcentage < 100 && localStorage.getItem("notiff") != null) {
                            localStorage.removeItem("notiff");
                        }
                        else {
                            setBatteryLevel(pourcentage, valeur202312);
                        }

                    }
                    else {
                        console.log("Erreur");
                    }
                })
        })
}

setInterval(resltatKw, 2000);