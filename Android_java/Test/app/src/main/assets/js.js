var adresse = "https://us-central1-boulou-functions-for-devs.cloudfunctions.net/";
var deviceId = "bf02db56c7a8be675baaey";
var devId = "-Nlm4vKk3psfiVmtLOhE";
var mail = "jrmanouhoseah@gmail.com";
var etatAction = adresse + "boulou_switch_device";
var etatActuel = "https://us-central1-boulou-functions-for-devs.cloudfunctions.net/boulou_check_deviceStatus?developerId=" + devId + "&email=" + mail + "&deviceId=" + deviceId;
var on = "ON";
var off = "OFF";
var ip = "192.168.88.21:8080"
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
    let heure = document.getElementById('heureH').value;
    let minute = document.getElementById('minuteH').value;
    let seconde = document.getElementById('secondeH').value;
    let action = document.getElementById('actionH').value;
    AjoutScene("scene", deviceId, heure, minute, seconde, action);
}
function sceneTimer() {
    ////////////////////////////
    let heure = document.getElementById('heureT').value;
    let minute = document.getElementById('minuteT').value;
    let action = document.getElementById('actionT').value;
    let seconde = document.getElementById('secondeT').value;
    AjoutScene("timer", deviceId, heure, minute, seconde, action);
}

// function AjoutScene(type, deviceId, idH, idM, idS, action) {
//     var req = ip + urlSever + "temps=" + idH + ":" + idM + ":" + idS + "&serie=" + deviceId + "&action=" + action + "&type=" + type;
//     console.log(req);
//     fetch(req)
//         .then(response => response.json())
//         .then(data => {
//             var res = data.resultat;
//             if (res === "ko") {
//                 alert("Un problème est survenu lors de l'ajout");
//             }
//             else {
//                 alert("Ajout de scène réussi");
//             }
//         })
//         .catch(error => {
//             console.error('Une erreur s\'est produite:', error);
//         });
// }
function AjoutScene() {
    let heure = document.getElementById('heureH').value;
    let minute = document.getElementById('minuteH').value;
    let seconde = document.getElementById('secondeH').value;

    var req = ip + urlSever + "temps=" + heure + ":" + minute + ":" + seconde + "&serie=" + deviceId + "&action=" + "off" + "&type=" + "scene";
    console.log(req)
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