<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/css/bootstrap.min.css"
        integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.2/font/bootstrap-icons.css"
        integrity="sha384-b6lVK+yci+bfDmaY1u0zE8YYJt0TZxLEAFyYSLHId4xoVvsrQu3INevFKo+Xir8e" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <link rel="stylesheet" href="styles.css" />
    <title>BOULOU</title>

</head>

<body>
    <nav style="background-color: #efefef;">
        <div class="form-group mb-2 d-flex justify-content-center align-items-center">
            <h2 style="color: #444;font-size: 2.5rem;
            font-weight: 1000;">Craft IT <i id="online" style="font-size: 30px;color: #007bff;"></i>
            </h2>
        </div>
        <div class="form-group mb-2 d-flex justify-content-center align-items-center">
            <button id="monBouton" type="button" class="neo"
                style="color: #007bff;padding: 10px;font-weight: 1000;margin: 10px;" data-bs-toggle="modal"
                data-bs-target="#exampleModal" style="margin-top: 10px; width: 200px; height: 45px;">
                <i class="bi bi-plus-circle-fill" style="font-size: 30px;"></i>
            </button>

            <button type="button" class="neo" style="color: #007bff;padding: 10px;font-weight: 1000;margin-right: 10px;"
                data-bs-toggle="modal" data-bs-target="#exampleModalScene"
                style="margin-top: 10px; width: 200px; height: 45px;" onclick="noneAfficherModal()">
                <i class="bi bi-alarm-fill" style="font-size: 30px;"></i>
            </button>


            <!-- <button type="button" class="neo" style="color: #00ff80;padding: 10px;font-weight: 1000;margin-right: 10px;"
                style="margin-top: 10px; width: 200px; height: 45px;" onclick="getData()">
                Actualiser
            </button> -->

            <button type="button" id="Message" class="neo"
                style="color: #007bff;padding: 10px;font-weight: 1000;margin-right: 10px;"
                style="margin-top: 10px; width: 200px; height: 45px;" onclick="navigateToMessageActivity()">
                <i class="bi bi-chat-left-text-fill" style="font-size: 30px;"></i>
            </button>
            <button type="button" id="Details" class="neo"
                style="color: #007bff;padding: 10px;font-weight: 1000;margin-right: 10px;"
                style="margin-top: 10px; width: 200px; height: 45px;" data-bs-toggle="modal"
                data-bs-target="#exampleModalDetails">
                <i class="bi bi-graph-down" style="font-size: 30px;"></i>
            </button>
            <button type="button" id="Details" class="neo" style="color: #007bff;padding: 10px;font-weight: 1000;"
                style="margin-top: 10px; width: 200px; height: 45px;" onclick="reload()">
                <i class="bi bi-repeat" style="font-size: 30px;"></i>
            </button>
        </div>
    </nav>


    <center>
        <div class="row d-flex flex-column align-items-center p-2">
            <div class="row col-md-12">
                <div class="card neo" style="overflow: auto;">
                    <div class="card-body">

                        <div class="switch">
                            <input type="checkbox" onchange="switchButton()" id="switchToggle" />
                            <label for="switchToggle"></label>
                        </div>

                    </div>
                </div>
            </div>
        </div>
        <div class="row d-flex flex-column align-items-center p-2">
            <div class="row col-md-12">
                <div class="card neo" style="overflow: auto;">
                    <div class="card-body">
                        <div class="form-group mb-2 d-flex justify-content-center align-items-center">
                            <input type="date" class="form-control col-md-3" id="daty">
                            <button class="btn btn-primary" onclick="getDay()">filtrer</button>
                        </div>

                        <canvas id="myChart" height="72%"></canvas>

                    </div>
                </div>
            </div>
        </div>
    </center>

    <div class="modal fade" id="exampleModalScene" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button class="neo ModalHeaderBtn" onclick="afficherContenu('horaireAff')">Horaire</button>
                    <button class="neo ModalHeaderBtn" onclick="afficherContenu('TimerAff')">Timer</button>
                    <button class="neo ModalHeaderBtn" onclick="afficherContenu('ConsoAff')">Consommation</button>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Fermer"></button>
                </div>
                <div id="horaireAff">
                    <div class="modal-body">
                        <h3 class="center">Horaire</h3>
                        <div class="ligne">
                            <input type="date" id="dateH" placeholder="hh" class="form-control" />
                            <label class="deuxPoints">/</label>
                            <input type="text" id="heureH" placeholder="hh" class="form-control" />
                            <label class="deuxPoints">:</label>
                            <input type="text" id="minuteH" placeholder="mm" class="form-control" />
                            <label class="deuxPoints">:</label>
                            <input type="text" id="secondeH" placeholder="ss" class="form-control" />
                        </div>
                        <br><br>

                        <select id="actionH" class="form-control">
                            <option value="on">ON</option>
                            <option value="off">OFF</option>
                        </select>
                        <br><br>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="neo" style="color: #ff0000;padding: 10px;font-weight: 1000;"
                            data-bs-dismiss="modal">Fermer</button>
                        <button type="button" class="neo" style="color: #007bff;padding: 10px;font-weight: 1000;"
                            onclick="sceneHoraire()" data-bs-dismiss="modal">Enregistrer</button>
                    </div>
                </div>
                <div id="TimerAff">
                    <div class="modal-body">
                        <h3 class="center">Timer</h3>
                        <div class="ligne">
                            <input type="text" id="heureT" placeholder="hh" class="form-control" />
                            <label class="deuxPoints">:</label>
                            <input type="text" id="minuteT" placeholder="mm" class="form-control" />
                            <label class="deuxPoints">:</label>
                            <input type="text" id="secondeT" placeholder="ss" class="form-control" />
                        </div>
                        <br><br>

                        <select id="actionT" class="form-control">
                            <option value="on">ON</option>
                            <option value="off">OFF</option>
                        </select>
                        <br><br>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="neo" style="color: #ff0000;padding: 10px;font-weight: 1000;"
                            data-bs-dismiss="modal">Fermer</button>
                        <button type="button" class="neo" style="color: #007bff;padding: 10px;font-weight: 1000;"
                            onclick="sceneTimer()" data-bs-dismiss="modal">Enregistrer</button>
                    </div>
                </div>
                <div id="ConsoAff">
                    <div class="modal-body">
                        <h3 class="center">Consommation</h3>
                        <label class="deuxPoints">Seuil en KW</label>
                        <input type="text" id="consom" placeholder="en KiloWatt" class="form-control" />
                        <label class="deuxPoints">Action OFF</label>
                        <br><br>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="neo" style="color: #ff0000;padding: 10px;font-weight: 1000;"
                            data-bs-dismiss="modal">Fermer</button>
                        <button type="button" class="neo" style="color: #007bff;padding: 10px;font-weight: 1000;"
                            onclick="seuilPost()" data-bs-dismiss="modal">Enregistrer</button>
                    </div>
                </div>


            </div>
        </div>
    </div>

    <div class="modal fade" id="exampleModalDetails" tabindex="-1" aria-labelledby="exampleModalLabel"
        aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-body">
                    <h3 class="center">DETAILS</h3>

                    <div id="page-wrap">
                        <h1 id="battery-level"> </h1>
                        <div class="meter nostripes">
                            <span id="battery-meter"></span>
                        </div>
                    </div>

                    <table class="table">
                        <thead>
                            <tr>
                                <th>Horaire</th>
                                <th>Action</th>
                                <th>Type</th>
                            </tr>
                        </thead>
                        <tbody id="sceneTableau">

                        </tbody>
                    </table>
                </div>
            </div>
        </div>

</body>
<script src="js.js"></script>

</html>

</body>

</html>