const panneauAlerte = document.getElementById("panneau-alerte");

if (isInternetExplorer()) {
  // Utilisation de className pour supporter IE9
  panneauAlerte.className += " panneau-alerte--visible";
}

function isInternetExplorer() {
  const userAgent = window.navigator.userAgent;
  const isIe10OuMoins = userAgent.indexOf("MSIE ") > 0;
  const isIe11 = userAgent.indexOf("Trident/") > 0;
  return isIe10OuMoins || isIe11;
}
