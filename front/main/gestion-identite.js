export async function normaliserPuisHasherIdentite(nom, prenom, dateNaissance, lieuNaissance) {
  const identiteeNormee = normaliserIdentite(nom, prenom, dateNaissance, lieuNaissance);
  return hasherSha512(identiteeNormee);
}

export async function hasherSha512(texte) {
  const encoder = new TextEncoder();
  const texteAsUnicode = encoder.encode(texte);

  const hashBinaire = await crypto.subtle.digest("SHA-512", texteAsUnicode);
  return transformerEnBase64(hashBinaire);
}

export function normaliserIdentite(nom, prenom, dateNaissance, lieuNaissance) {
  const nomNorme = normaliserTexte(nom);
  const prenomNorme = normaliserTexte(prenom);
  const lieuNaissanceNorme = normaliserTexte(lieuNaissance);
  // la gestion des dates est trop aléatoire à partir d'un input pas contrôlé, on préfèrera des règles sur les input
  const dateNaissanceNormee = dateNaissance;

  return nomNorme + prenomNorme + dateNaissanceNormee + lieuNaissanceNorme;
}

function transformerEnBase64(hashBinaire) {
  const iterableHashArray = new Uint8Array(hashBinaire);
  let binaryString = "";
  for (let character of iterableHashArray) {
    binaryString += String.fromCharCode(character);
  }
  return btoa(binaryString);
}

function normaliserTexte(texte) {
  const caracteresCombines = /[\u0300-\u036F]/g; // accents et autres combinaisons pour créer un autre caractère
  const espaces = [/[\u0000-\u0020]/g, /[\u008f-\u0090]/g, "\u009d", "\u00a0", "\u00ad"];
  const caracteresInterdits = [/['\-$|!:;_.<]/g];

  let texteFinal = texte.normalize("NFKD").replace(caracteresCombines, "").toUpperCase();

  const caracteresARemplacer = caracteresInterdits.concat(espaces);
  caracteresARemplacer.forEach(
    (rangeCaracteresARemplacer) => (texteFinal = texteFinal.replace(rangeCaracteresARemplacer, ""))
  );

  return texteFinal;
}
