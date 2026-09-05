
"SELECT * FROM Achat "
"SELECT * FROM Achat  WHERE  date_achat= ? "
"SELECT * FROM Achat  WHERE  id_client= ? "


"SELECT * FROM Achat_detaille "
"SELECT * FROM Achat_detaille  WHERE  id_achat= ? "
 
"SELECT * FROM  Versement_Entreprise "
"SELECT * FROM Versement_Entreprise  WHERE  id_entreprise= ? "
"SELECT * FROM Versement_Entreprise  WHERE  date_versement= ? "



SELECT [Client.nom] & " " & [Client.prenom] AS FullName,Client.matricule,
 Achat.prix_total,Achat.date_achat FROM Achat INNER JOIN Client ON Achat.id_client = Client.id 
WHERE Client.id_entreprise = ? 
AND Achat.id NOT IN (
    SELECT id_achat FROM Client_Paye_Par_Entreprise WHERE id_achat IS NOT NULL
    );


SELECT Client.nom & " " & Client.prenom AS FullName,Client.matricule,
Produit.designation , Achat_detaille.qty,
Achat_detaille.prix_total

FROM  (((Client INNER JOIN Achat ON Client.id  = Achat.id_client)
INNER JOIN  Achat_detaille ON Achat.id = Achat_detaille.id_achat )
INNER JOIN Produit ON Achat_detaille.id_produit = Produit.id)
WHERE Client.id_entreprise = $P{ENTERPRISE_ID}
AND Achat.id NOT IN (
    SELECT Client_Paye_Par_Entreprise.id_achat FROM Client_Paye_Par_Entreprise WHERE Client_Paye_Par_Entreprise.id_achat IS NOT NULL
);