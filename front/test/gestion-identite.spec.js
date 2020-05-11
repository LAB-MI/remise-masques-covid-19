const GestionIdentite = require("../main/gestion-identite");

test("normaliserIdentite devrait enlever les espaces et mettre en majuscule", () => {
  expect(GestionIdentite.normaliserIdentite("Rives", "Jean Michel", "22/03/1967", "Le Mans")).toBe(
    "RIVESJEANMICHEL22/03/1967LEMANS"
  );
});

test("normaliserIdentite devrait enlever choses qui ressemblent a des espaces mais qui n'en sont pas", () => {
  // L'espace dans "JeanMichel" n'est pas un vrai espace mais 'END OF TRANSMISSION' : \u0004
  expect(GestionIdentite.normaliserIdentite("O'Hara", "JeanMichel", "22/03/1967", "Le Mans")).toBe(
    "OHARAJEANMICHEL22/03/1967LEMANS"
  );
});

test("normaliserIdentite devrait enlever les apostrophes", () => {
  expect(GestionIdentite.normaliserIdentite("O'Hara", "JeanMichel", "22/03/1967", "Le Mans")).toBe(
    "OHARAJEANMICHEL22/03/1967LEMANS"
  );
});

test("normaliserIdentite devrait enlever les tirets", () => {
  expect(GestionIdentite.normaliserIdentite("Rives", "Jean-Michel", "22/03/1967", "Le Mans")).toBe(
    "RIVESJEANMICHEL22/03/1967LEMANS"
  );
});

test("normaliserIdentite devrait enlever les underscore", () => {
  expect(GestionIdentite.normaliserIdentite("Rives", "Jean_Michel", "22/03/1967", "Le_Mans")).toBe(
    "RIVESJEANMICHEL22/03/1967LEMANS"
  );
});

test("normaliserIdentite devrait enlever les points", () => {
  expect(GestionIdentite.normaliserIdentite("M.Rives", "JeanMichel", "22/03/1967", "Le_Mans")).toBe(
    "MRIVESJEANMICHEL22/03/1967LEMANS"
  );
});

test("normaliserIdentite devrait enlever plein de caractères supplémentaires : $|!:;<", () => {
  expect(GestionIdentite.normaliserIdentite("Rives$|!:;<", "JeanMichel", "22/03/1967", "Le_Mans")).toBe(
    "RIVESJEANMICHEL22/03/1967LEMANS"
  );
});

test("normaliserIdentite devrait remplacer les caractères accentués par des équivalents non accentués", () => {
  expect(GestionIdentite.normaliserIdentite("Rivès", "JeanMichel", "22/03/1967", "Le Mans")).toBe(
    "RIVESJEANMICHEL22/03/1967LEMANS"
  );
});

test("hasherSha512 devrait hasher en SHA-512 et encoder en base64", () => {
  window.crypto.subtle.digest.mockReturnValue("");

  GestionIdentite.hasherSha512("RIVESJEANMICHEL22/03/1967LEMANS");
  expect(window.crypto.subtle.digest).toHaveBeenCalledWith("SHA-512", "RIVESJEANMICHEL22/03/1967LEMANS");
});
