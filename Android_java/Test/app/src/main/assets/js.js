function navigateToMessageActivity() {
    Android.navigateMessage();
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
var ip = "http://192.168.88.21:8080"
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
function getData() {
    var checkbox = document.getElementById('switchToggle');
    fetch(etatActuel)
        .then(response => response.json())
        .then(itema => {
            var success = itema.success;
            if (success == false) {
                alert("Un problème est survenu lors de la recuperation de la liste");
            } else {
                var state = itema.result.status.switch;
                if (state == true) {
                    checkbox.checked = true;
                }
                else if (state == false) {
                    checkbox.checked = false;
                }
                console.log(state);
            }
        })
        .catch(error => {
            console.error('Une erreur s\'est produite:', error);
        });
}
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
    var req = "http://192.168.88.21:8080/iot/scene/lister?serie=bf02db56c7a8be675baaey";
    fetch(req)
        .then(response => response.json())
        .then(data => {
            var res = data.resultat;

            if (res === "ko") {
                alert("Un problème est survenu lors de la recuperation de la liste");
            } else {
                console.log(data);
            }
        })
}

listeScene();