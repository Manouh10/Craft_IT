var adresse = "https://us-central1-boulou-functions-for-devs.cloudfunctions.net/";
var deviceId = "bf02db56c7a8be675baaey";
var devId = "-Nlm4vKk3psfiVmtLOhE";
var mail = "jrmanouhoseah@gmail.com";
var etatAction = adresse + "boulou_switch_device";
var etatActuel = "https://us-central1-boulou-functions-for-devs.cloudfunctions.net/boulou_check_deviceStatus?developerId=" + devId + "&email=" + mail + "&deviceId=" + deviceId;
var on = "ON";
var off = "OFF";
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

function ok() {
    var check = document.getElementById('switchToggle').checked;
    if (check == false) {
        etat(off);
    } else {
        etat(on);
    }
}


function AjoutScene() {
    var idHeure = document.getElementById('serieHeure').value;
    var heure = document.getElementById('heure').value;
    var minute = document.getElementById('minute').value;
    var action = document.getElementById('action').value;
    var type = document.getElementById('type').value;

    var req = sceneIp + "temps=" + heure + ":" + minute + ":00" + "&serie=" + idHeure + "&action=" + action;
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
