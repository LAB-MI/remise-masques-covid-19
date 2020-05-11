const GestionIdentite = require("./gestion-identite");

const formulaireBonPerception = document.getElementById("formulaire-bon-perception");
const actualFormulaireBonPerception = document.getElementById("actual-formulaire-bon-perception");

const tousLesInputs = formulaireBonPerception.querySelectorAll("input");
const tempsAttenteTexteInfo = 2000;
let timeoutTexteInfo;
let timeoutFinAnimation;
let main = document.getElementById("main-formulaire");
let modalScannerDialog = document.getElementById("modal-scanner");
let fermerModal = document.getElementById("fermer-modal-scanner");
let canvasElement = document.getElementById("canvas-scanner");
let canvas = canvasElement.getContext("2d");
let video = document.createElement("video");
let boutonScanner = document.getElementById("ouvrir-qr-scanner");
let infoScanBox = document.getElementById("info-scan");
let modeSaisieElement = document.getElementById("champ-mode-saisie");
let dateElement = document.getElementById("champ-date-naissance");
let etat = "ferme";
let codeClavierEchap = "Escape";
let codeClavierTab = "Tab";

function afficherHeadersInfo() {
  document.querySelectorAll(".modal-header-info").forEach((header) => header.classList.add("afficher"));
}

function cacherHeadersInfo() {
  document.querySelectorAll(".modal-header-info").forEach((header) => header.classList.remove("afficher"));
}

function attendreFinAnimation() {
  timeoutFinAnimation = window.setTimeout(() => {
    fermerModal.focus();
    fermerModal.addEventListener("keydown", listnerFocusTrap());
  }, 100);
}

let codeClavierEntree = "Enter";
boutonScanner.addEventListener("click", (event) => {
  event.preventDefault();
  changerEtat();
});
boutonScanner.addEventListener("keydown", (event) => {
  if (event.key === codeClavierEntree) {
    event.preventDefault();
    changerEtat();
  }
});
infoScanBox.addEventListener("click", () => changerEtat());
fermerModal.addEventListener("click", (event) => {
  event.preventDefault();
  changerEtat();
});

modalScannerDialog.addEventListener("keydown", (event) => {
  if (event.key === codeClavierEchap) {
    changerEtat();
  }
});

window.addEventListener("click", (event) => {
  if (event.target === modalScannerDialog) changerEtat();
});

boutonScanner.focus();
formulaireBonPerception.addEventListener("submit", (event) => transformerIdentite(event));

dateElement.addEventListener("keyup", ajouterUnSlashPendantRedactionDate, false);

tousLesInputs.forEach((input) => {
  input.addEventListener("input", mettreAJourExemple);
});

async function transformerIdentite(event) {
  event.preventDefault();

  const prenom = document.getElementById("champ-prenom").value;
  const nom = document.getElementById("champ-nom").value;
  const dateNaissance = document.getElementById("champ-date-naissance").value;
  const lieuNaissance = document.getElementById("champ-lieu-naissance").value;
  const champHashIdentite = document.getElementById("champ-hash-identite");

  document.getElementById("champ-nombre-mineurs-hidden").value = document.getElementById("champ-nombre-mineurs").value;
  const identiteHashee = await GestionIdentite.normaliserPuisHasherIdentite(nom, prenom, dateNaissance, lieuNaissance);
  champHashIdentite.value = identiteHashee;
  actualFormulaireBonPerception.submit();
}

function changerEtat(statut) {
  if (etat === "ferme") {
    ouvrirQRScanner();
  } else {
    fermerQRScanner();
    changerStyleBouton(boutonScanner, statut);
  }
}

function changerStyleBouton(bouton, statut) {
  if (statut === "Succes") {
    bouton.classList.remove("erreur");
    bouton.classList.add("succes");
    bouton.innerHTML = '<img src="/btn-check.svg" alt="Icone check" class="scan"/> QR code scanné';
  } else if (statut === "Erreur") {
    bouton.classList.remove("succes");
    bouton.classList.add("erreur");
    bouton.innerHTML = '<img src="/error.png" alt="Icone erreur" class="scan"/> QR code invalide';
  } else {
    bouton.classList.remove("succes");
    bouton.classList.remove("erreur");
    bouton.innerHTML = '<img src="/scan.svg" alt="Icône scan" class="scan"/> Scanner le QR code';
  }
}

function ouvrirQRScanner() {
  navigator.mediaDevices
    .getUserMedia({ video: { facingMode: "environment" } })
    .then(function (stream) {
      etat = "ouvert";
      modalScannerDialog.setAttribute("aria-hidden", false);
      main.setAttribute("aria-hidden", true);
      attendreFinAnimation();
      video.srcObject = stream;
      video.setAttribute("playsinline", true);
      video.play();
      setTimeoutTexteInfo();
      requestAnimationFrame(scanner);
    })
    .catch(() => {});
}

function listnerFocusTrap() {
  return (event) => {
    const tab = event.key === codeClavierTab;

    if (!tab) {
      return;
    }
    event.preventDefault();
  };
}

function fermerQRScanner() {
  if (video.srcObject) {
    video.srcObject.getTracks().forEach((track) => track.stop());
  }
  canvas.clearRect(0, 0, canvasElement.width, canvasElement.height);
  etat = "ferme";
  window.clearTimeout(timeoutFinAnimation);
  window.clearTimeout(timeoutTexteInfo);
  fermerModal.removeEventListener("keydown", listnerFocusTrap);
  modalScannerDialog.setAttribute("aria-hidden", true);
  main.setAttribute("aria-hidden", false);
  cacherHeadersInfo();
  fermerModal.classList.remove("lignes-2");
  boutonScanner.focus();
}

function scanner() {
  if (video.readyState === video.HAVE_ENOUGH_DATA) {
    canvas.drawImage(video, 0, 0, canvasElement.width, canvasElement.height);
    const imageData = canvas.getImageData(0, 0, canvasElement.width, canvasElement.height);
    const qrCode = jsQR(imageData.data, imageData.width, imageData.height, {
      inversionAttempts: "dontInvert",
    });
    if (qrCode) {
      const status = remplirFormulaire(qrCode.data);
      status === "Succes" ? (modeSaisieElement.value = "SCAN") : (modeSaisieElement.value = "MANUEL");
      infoScanBox.style.display = "grid";
      changerEtat(status);
    } else if (etat === "ouvert") {
      requestAnimationFrame(scanner);
    }
  } else if (etat === "ouvert") {
    requestAnimationFrame(scanner);
  }
}

function setTimeoutTexteInfo() {
  timeoutTexteInfo = window.setTimeout(() => {
    afficherHeadersInfo();
    fermerModal.classList.add("lignes-2");
  }, tempsAttenteTexteInfo);
}

function remplirFormulaire(data) {
  let parsedData;
  try {
    parsedData = JSON.parse(data);
  } catch (e) {
    return "Erreur";
  }
  let champsARemplir = ["prenom", "nom", "dateNaissance", "lieuNaissance", "nbMineurs"];
  let champUndefined = champsARemplir.find(function (entry) {
    return parsedData[entry] === undefined;
  });
  if (champUndefined) return "Erreur";

  document.getElementById("champ-prenom").value = parsedData.prenom;
  document.getElementById("champ-nom").value = parsedData.nom;
  document.getElementById("champ-date-naissance").value = parsedData.dateNaissance;
  document.getElementById("champ-lieu-naissance").value = parsedData.lieuNaissance;
  document.getElementById("champ-nombre-mineurs").value = parsedData.nbMineurs;
  return "Succes";
}

function ajouterUnSlashPendantRedactionDate(evenement) {
  const codeTouche = evenement.keyCode || evenement.charCode;
  const codeBackspace = 8;
  const codeDelete = 46;

  if (codeTouche !== codeBackspace && codeTouche !== codeDelete) {
    ajouterUnSlashEnFin(evenement.target);
  }
}

function ajouterUnSlashEnFin($dateInput) {
  $dateInput.value = $dateInput.value.replace(/^(\d{2})$/g, "$1/");
  $dateInput.value = $dateInput.value.replace(/^(\d{2})\/(\d{2})$/g, "$1/$2/");
  $dateInput.value = $dateInput.value.replace(/\/\//g, "/");
}

let inputTimezone = document.getElementById("timezone");
inputTimezone.value = Intl.DateTimeFormat().resolvedOptions().timeZone;

function mettreAJourExemple(evenement) {
  const input = evenement.target;
  const exemple = input.parentNode.querySelector(".exemple");
  if (input.placeholder && exemple) {
    if (input.value) {
      exemple.innerHTML = "ex.&nbsp;: " + input.placeholder;
    } else {
      exemple.innerHTML = "";
    }
  }
}
