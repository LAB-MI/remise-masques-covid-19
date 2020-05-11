const formulaireDistributionMasques = document.getElementById("formulaire-distribution-masques");
const tousLesChampsSaisieLibreNombreDeMasques = formulaireDistributionMasques.querySelectorAll('input[type="number"]');
const tousLesBoutonsNombreDeMasques = formulaireDistributionMasques.querySelectorAll('input[type="radio"]');

tousLesChampsSaisieLibreNombreDeMasques.forEach((champSaisieLibreNbMasques) => {
  champSaisieLibreNbMasques.addEventListener("input", (event) => {
    uniformiserChampSaisieLibreEtChampFormulaireEtDecocherBouton(event);
    mettreAJourBaliseExemple(event.target);
  });
});

tousLesBoutonsNombreDeMasques.forEach((boutonNombreDeMasques) => {
  boutonNombreDeMasques.addEventListener("click", (event) => {
    uniformiserBoutonEtChampFormulaireEtViderChampSaisieLibre(event);
  });
});

formulaireDistributionMasques.addEventListener("submit", revaloriserEtNettoyerFormulaire);

function uniformiserChampSaisieLibreEtChampFormulaireEtDecocherBouton(event) {
  const eventTargetScope = event.target.parentElement.parentElement;
  const nbMasquesEnvoyesAuFormulaire = eventTargetScope.querySelector('input[type="hidden"]');
  const boutonsNombreDeMasques = eventTargetScope.querySelectorAll('input[type="radio"]');

  nbMasquesEnvoyesAuFormulaire.value = event.target.value;
  boutonsNombreDeMasques.forEach((bouton) => {
    bouton.checked = false;
  });
}

function uniformiserBoutonEtChampFormulaireEtViderChampSaisieLibre(event) {
  const eventTargetScope = event.target.parentElement.parentElement.parentElement;
  const nbMasquesEnvoyesAuFormulaire = eventTargetScope.querySelector('input[type="hidden"]');
  const champNombreDeMasques = eventTargetScope.querySelector('input[type="number"]');

  nbMasquesEnvoyesAuFormulaire.value = event.target.value;
  champNombreDeMasques.value = "";

  mettreAJourBaliseExemple(champNombreDeMasques);
}

function revaloriserEtNettoyerFormulaire() {
  tousLesChampsSaisieLibreNombreDeMasques.forEach((champSaisieLibreNbMasques) => {
    const champNbMasquesEnvoyesAuFormulaire = champSaisieLibreNbMasques.parentElement.querySelector(
      'input[type="hidden"]'
    );
    const boutonRadioNbMasques = champSaisieLibreNbMasques.parentElement.parentElement.querySelector(
      'input[type="radio"]:checked'
    );

    if (champSaisieLibreNbMasques.value > 0) {
      champNbMasquesEnvoyesAuFormulaire.value = champSaisieLibreNbMasques.value;
    } else if (boutonRadioNbMasques != null) {
      champNbMasquesEnvoyesAuFormulaire.value = boutonRadioNbMasques.value;
    } else {
      champNbMasquesEnvoyesAuFormulaire.value = 0;
    }
  });

  tousLesBoutonsNombreDeMasques.forEach((bouton) => {
    bouton.setAttribute("disabled", true);
  });
}

function mettreAJourBaliseExemple(input) {
  const exemple = input.parentNode.querySelector(".exemple");
  if (input.placeholder && exemple) {
    if (input.value) {
      exemple.innerHTML = "ex.&nbsp;: " + input.placeholder;
    } else {
      exemple.innerHTML = "";
    }
  }
}
