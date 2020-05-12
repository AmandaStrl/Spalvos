package com.example.spalvos;

public class Uzsakymai {

    String vardas,elPastas,kodas;
    String numeris,kiekis,talpa;

        public Uzsakymai(String vardas, String elPastas, String kodas, String numeris, String kiekis, String talpa) {

        this.vardas = vardas;
        this.elPastas = elPastas;
        this.kodas = kodas;
        this.numeris = numeris;
        this.kiekis = kiekis;
        this.talpa = talpa;
    }

    public String getVardas() {
        return vardas;
    }

    public String getElPastas() {
        return elPastas;
    }

    public String getKodas() {
        return kodas;
    }

    public String getNumeris() {
        return numeris;
    }

    public String getKiekis() {
        return kiekis;
    }

    public String getTalpa() {
        return talpa;
    }

    public String toString() {
            return vardas+ " " + numeris;
    }
}
