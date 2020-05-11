import "cookieconsent";

if (process.env.ACTIVATE_ANALYTICS === "true") {
  window.cookieconsent.initialise({
    palette: {
      popup: {
        background: "#000191",
        text: "#fff",
      },
      button: {
        background: "#fff",
        text: "#000191",
      },
    },
    type: "opt-out",
    content: {
      policy: "Gestion des Cookies",
      message: "Ce site utilise des cookies afin d’améliorer ses fonctionnalités et sa navigation.",
      allow: "J'accepte",
      deny: "Je refuse",
      link: "En savoir plus",
      href: "https://distribution-masques-covid-19.interieur.gouv.fr/confidentialite.html",
      target: "_blank",
    },
    law: {
      countryCode: "FR",
    },
    onStatusChange(_status, _chosenBefore) {
      if (this.hasConsented()) {
        activerAtInternet();
      }
    },
  });
}

function activerAtInternet() {
  const idClientAtInternet = "445732";
  const idSiteDistribution = "32";
  // ATInternet importé par https://tag.aticdn.net/445732/smarttag.js
  // Bloqué par AdBlock et autres extensions, donc il faut être défensif
  // eslint-disable-next-line no-undef
  if (typeof ATInternet === "undefined") {
    return null;
  }
  // eslint-disable-next-line no-undef
  const tagAtInternet = new ATInternet.Tracker.Tag({
    site: idClientAtInternet,
    secure: "on",
  });
  tagAtInternet.page.set({
    level2: idSiteDistribution,
    name: "remise-masques-covid-19",
  });
  tagAtInternet.dispatch();
}

const formulaireBonPerception = document.getElementById("formulaire-identification");
const tousLesInputs = formulaireBonPerception.querySelectorAll("input");

tousLesInputs.forEach((input) => {
  input.addEventListener("input", mettreAJourExemple);
});

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
