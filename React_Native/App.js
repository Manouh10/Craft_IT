import React, { useState, useEffect } from 'react';
import { View, TextInput, Button, Text } from 'react-native';
import axios from 'axios';
import { request, PERMISSIONS } from 'react-native-permissions';
import SmsAndroid from 'react-native-get-sms-android';
import SQLite from 'react-native-sqlite-storage';
import KeepAwake from 'react-native-keep-awake';

KeepAwake.activate();

var adresse = "https://us-central1-boulou-functions-for-devs.cloudfunctions.net/";
var deviceId = "bf02db56c7a8be675baaey";
var devId = "-Nlm4vKk3psfiVmtLOhE";
var mail = "jrmanouhoseah@gmail.com";
var etatAction = adresse + "boulou_switch_device";
var etatActuel = "https://us-central1-boulou-functions-for-devs.cloudfunctions.net/boulou_check_deviceStatus?developerId=" + devId + "&email=" + mail + "&deviceId=" + deviceId;
var on = "ON";
var off = "OFF";
var ip = "http://192.168.1.149:8080";
var urlSever = "/iot/scene/creer?";

const db = SQLite.openDatabase({ name: 'hackaton.db', location: 'default' });

function createTable() {
  db.transaction(function (tx) {
    tx.executeSql(
      'CREATE TABLE IF NOT EXISTS smsvaleur (id INTEGER PRIMARY KEY AUTOINCREMENT, body TEXT, etat INTEGER, date TEXT);',
      [],
      (tx, results) => {
        //console.log('Table smsvaleur créée avec succès');
      },
      (tx, error) => {
        console.error('Erreur lors de la création de la table :', error.message);
      }
    );
  });
}
db.transaction((tx) => {
  tx.executeSql(
    'CREATE TABLE IF NOT EXISTS numandefa (id INTEGER PRIMARY KEY AUTOINCREMENT, numero TEXT)',
    [],
    (tx, results) => {
      // console.log('Table numandefa créée avec succès');
    },
    (error) => {
      console.error('Erreur lors de la création de la table : ', error);
    }
  );
});

const verifierNombreNumeros = async () => {
  return new Promise((resolve, reject) => {
    db.transaction((tx) => {
      tx.executeSql(
        'SELECT COUNT(*) AS count FROM numandefa',
        [],
        (tx, results) => {
          const count = results.rows.item(0).count;
          resolve(count);
        },
        (error) => {
          reject(error);
        }
      );
    });
  });
};

function updateDataById(id, etat) {
  db.transaction(tx => {
    tx.executeSql(
      'UPDATE smsvaleur SET etat = ? WHERE id = ?',
      [etat, id],
      (tx, results) => {
        if (results.rowsAffected > 0) {
          console.log('Mise à jour réussie');
        } else {
          console.log('Aucune ligne mise à jour');
        }
      },
      error => {
        console.error('Erreur lors de la mise à jour:', error);
      }
    );
  });
}
// function envoyerDonnees(valeur, id, etat) {
//   axios.get(`http://192.168.4.1/data?keys=${valeur}`)
//     .then(response => {
//       console.log(response.data);
//       updateDataById(id, etat);
//     })
//     .catch(error => {
//       console.error(error);
//     });
// };

function etat(action, id, etatt) {
  const data = {
    developerId: devId,
    email: mail,
    deviceId: deviceId,
    switch_status: action
  };
  axios.post(etatAction, data, {
    headers: {
      'Content-Type': 'application/json',
    }
  })
    .then(response => {
      console.log('Response:', response.data);
      updateDataById(id, etatt);
    })
    .catch(error => {
      // console.error('Error:', error);
    });
}

function AjoutScene(type, deviceId, idH, idM, idS, action, id, etatt) {
  var req = ip + urlSever + "temps=" + idH + ":" + idM + ":" + idS + "&serie=" + deviceId + "&action=" + action + "&type=" + type;
  console.log(req);

  axios.get(req)
    .then(response => {
      var res = response.data.resultat;
      if (res === "ko") {
        console.log("Un problème est survenu lors de l'ajout");
      } else {
        console.log("Ajout de scène réussi");
        updateDataById(id, etatt);
      }
    })
    .catch(error => {
      //console.error('Une erreur s\'est produite:', error);
    });
}


function insertMalagasyData(smsList) {
  smsList.forEach((sms) => {
    // const bodydy = sms.body.split("_");
    const body = sms.body.toUpperCase();
    const date = sms.date;
    const etat = 0;

    db.transaction(function (tx) {
      tx.executeSql(
        'SELECT * FROM smsvaleur WHERE body = ? AND date = ?;',
        [body, date],
        (tx, results) => {
          var rowCount = results.rows.length;
          if (rowCount === 0) {
            db.transaction(function (tx) {
              tx.executeSql(
                'INSERT INTO smsvaleur (body, etat, date) VALUES (?, ?, ?);',
                [body, etat, date],
                (tx, results) => {
                  console.log('SMS inséré avec succès : ' + date);
                },
                (tx, error) => {
                  console.error('Erreur lors de l\'insertion du SMS : ' + error.message);
                }
              );
            });
          } else {
            console.log('Le SMS existe déjà : ' + date);
          }
        },
        (tx, error) => {
          console.error('Erreur lors de la vérification du SMS : ' + error.message);
        }
      );
    });
  });
}
function retrieveSms(phoneNumber, setSmsList) {
  const filter = {
    box: 'inbox',
    address: phoneNumber,
  };
  SmsAndroid.list(
    JSON.stringify(filter),
    (fail) => console.error('Erreur lors de la récupération des SMS :', fail),
    (count, smsListArray) => {
      if (count > 0) {
        try {
          const smsJson = JSON.parse(smsListArray);
          setSmsList(smsJson);
          insertMalagasyData(smsJson);
        } catch (error) {
          console.error('Erreur lors de la conversion des SMS en JSON :', error.message);
        }
      } else {
        console.log('Aucun SMS trouvé.');
      }
    }
  );
}

function getSMSData() {
  db.transaction(function (tx) {
    tx.executeSql(
      'SELECT * FROM smsvaleur WHERE etat = 0;',
      [],
      (tx, results) => {
        var len = results.rows.length;
        if (len > 0) {
          for (let i = 0; i < len; i++) {
            var row = results.rows.item(i);
            //console.log('SMS:', row.body, 'Date:', row.date);
            //             var smsContent=
            var split = row.body;
            var dataSms = split.split("_");

            if (dataSms.length == 1 && dataSms[0] == "ALLUMER") {
              //allumer sec
              etat(on, row.id, 1);
            }
            else if (dataSms.length == 1 && dataSms[0] == "ETEINDRE") {
              // eteindre sec
              etat(off, row.id, 1);
            }

            // else if (dataSms.length == 2 && dataSms[0] == "ALLUMER") {
            //   //param 1 timer
            // }
            // else if (dataSms.length == 2 && dataSms[0] == "ETEINDRE") {
            //   //param 1 timer
            // }

            else if (dataSms.length == 4 && dataSms[0] == "ALLUMER") {
              //param 1 , 2 scene
              AjoutScene("scene", deviceId, dataSms[1], dataSms[2], dataSms[3], "on", row.id, 1)
            }
            else if (dataSms.length == 4 && dataSms[0] == "ETEINDRE") {
              //param 1 , 2 scene
              AjoutScene("scene", deviceId, dataSms[1], dataSms[2], dataSms[3], "off", row.id, 1)
            }

            // if (row.body == 'START1' || row.body == 'STOP1' || row.body == 'RESTART1' || row.body == 'START2' || row.body == 'STOP2' || row.body == 'RESTART2') {
            // envoyerDonnees(row.body, row.id, 1);
            // }

          }
        } else {
          console.log('Aucun SMS trouvé dans la base de données.');
        }
      },
      (tx, error) => {
        console.error('Erreur lors de la récupération des SMS depuis la base de données : ' + error.message);
      }
    );
  });
}

function App() {
  const [smsPermission, setSmsPermission] = useState(null);
  const [smsList, setSmsList] = useState([]);
  const [editingRowId, setEditingRowId] = useState(null);
  const [editedNumero, setEditedNumero] = useState('');
  const [tableData, setTableData] = useState([]);
  const [nouveauNumero, setNouveauNumero] = useState('');

  const ajouterNumero = async () => {
    const nombreMaxNumeros = 3;

    const nombreNumerosActuels = await verifierNombreNumeros();

    if (nombreNumerosActuels < nombreMaxNumeros) {
      db.transaction((tx) => {
        tx.executeSql(
          'INSERT INTO numandefa (numero) VALUES (?)',
          [nouveauNumero],
          () => {
            alert('Numéro ajouté');
            db.transaction((tx) => {
              tx.executeSql(
                'SELECT * FROM numandefa',
                [],
                (tx, { rows }) => {
                  const data = rows.raw();
                  setTableData(data);
                },
                (error) => {
                  console.error('Erreur lors de la récupération des données : ', error);
                }
              );
            });
          },
          (error) => {
            console.error('Error deleting record from the database:', error);
          }
        );
      });

      setNouveauNumero('');
    } else {
      alert('Limite atteinte, 3 numéros maximum');
    }
  };
  useEffect(() => {
    select();
  }, []);

  const select = () => {
    db.transaction((tx) => {
      tx.executeSql(
        'SELECT * FROM numandefa',
        [],
        (tx, { rows }) => {
          const data = rows.raw();
          setTableData(data);
        },
        (error) => {
          console.error('Erreur lors de la récupération des données : ', error);
        }
      );
    });
  };

  const deleteNumero = (id) => {
    const updatedTableData = tableData.filter((row) => row.id !== id);
    setTableData(updatedTableData);
    db.transaction((tx) => {
      tx.executeSql(
        'DELETE FROM numandefa WHERE id = ?',
        [id],
        () => {
          alert('Numéro supprimé');
        },
        (error) => {
          console.error('Error deleting record from the database:', error);
        }
      );
    });
  };
  const handleEdit = (rowId, initialValue) => {
    // Set the editingRowId and initialize editedNumero with the current value
    setEditingRowId(rowId);
    setEditedNumero(initialValue);
  };

  const handleSave = (rowId) => {

    db.transaction((tx) => {
      tx.executeSql(
        'UPDATE numandefa SET numero = ? WHERE id = ?',
        [editedNumero, rowId],
        () => {

          alert('Modifier avec succès!');
        },
        (error) => {
          // Handle the error
          console.error('Error updating record in the database:', error);
        }
      );
    });

    const updatedTableData = tableData.map((row) =>
      row.id === rowId ? { ...row, numero: editedNumero } : row
    );
    setTableData(updatedTableData);

    setEditingRowId(null);
    setEditedNumero('');
  };

  useEffect(() => {
    const fetchData = () => {
      createTable();
      db.transaction((tx) => {
        tx.executeSql(
          'SELECT * FROM numandefa',
          [],
          async (tx, { rows }) => {
            const data = rows.raw();
            for (const row of data) {
              const numero = row.numero;
              retrieveSms(numero, setSmsList);
            }
          },
          (error) => {
            console.error('Erreur lors de la récupération des données : ', error);
          }
        );
      });

      getSMSData();
    };
    fetchData();
    const intervalId = setInterval(fetchData, 5000);
    return () => {
      clearInterval(intervalId);
    };
  }, []);
  useEffect(() => {
    const checkPermission = async () => {
      const result = await request(PERMISSIONS.ANDROID.READ_SMS);

      if (result === 'granted') {
        console.log('Permission accordée pour accéder aux SMS.');
        setSmsPermission('granted');
      } else {
        console.log('Permission non accordée pour accéder aux SMS.');
        setSmsPermission('denied');
      }
    };

    checkPermission();
  }, []);



  return (
    <>
      <View style={{ flexDirection: 'row' }}>
        <View style={{ width: '85%', height: 50, marginTop: 20 }}>
          <TextInput
            style={{
              flex: 1,
              borderRadius: 10,
              borderWidth: 1,
              borderColor: 'black',
              paddingLeft: 11,
            }}
            value={nouveauNumero}
            onChangeText={(text) => setNouveauNumero(text)}
            placeholder="Ajouter un nouveau numéro récepteur"
          />
        </View>
        <View style={{ width: '3%' }}>

        </View>
        <View style={{
          width: '12%', marginTop: 32
        }}>
          <Button title='+'
            onPress={ajouterNumero}
          />
        </View>
      </View>
      <View style={{ marginTop: 20, alignItems: 'center' }}>
        {tableData.map((row) => (
          <View
            key={row.id}
            style={{
              flexDirection: 'row',
              borderBottomWidth: 1,
              borderColor: 'gray',
              marginTop: 10,
            }}
          >
            {editingRowId === row.id ? (
              <View style={{ flex: 1 }}>
                <TextInput
                  value={editedNumero}
                  onChangeText={(text) => setEditedNumero(text)}
                  onBlur={() => handleSave(row.id)}
                />
              </View>
            ) : (
              <View style={{ flex: 1 }}>
                <Text>{row.numero}</Text>
              </View>
            )}
            <View style={{ flex: 1 }}>
              {editingRowId === row.id ? (
                <Button title='Enregistrer' onPress={() => handleSave(row.id)} />
              ) : (
                <Button title='Modifier' onPress={() => handleEdit(row.id, row.numero)} />
              )}
            </View>
            <View style={{ flex: 1 }}>
              <Button title='Supprimer' onPress={() => deleteNumero(row.id)} />
            </View>
          </View>
        ))}
      </View>
    </>
  );
}

export default App;