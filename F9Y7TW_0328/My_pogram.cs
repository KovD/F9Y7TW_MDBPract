using System;
using System.Xml.Linq;
using System.Linq;

XDocument dokumentum = XDocument.Load("F9Y7TW_XML1.xml");
XElement gyoker = dokumentum.Descendants("vendeglatas").First();

Console.WriteLine("(0.) Dokumentum betöltve.");


Console.WriteLine("\n(1.) Budapesti luxusbárok:");
var bpBarok = gyoker.Descendants("luxusbar")
    .Where(elem => elem.Element("varos").Value == "Budapest").ToList();

foreach (var item in bpBarok)
{
    Console.WriteLine("-- " + item.Element("nev").Value);
}

Console.WriteLine("\n(2.) Termékek és beszállítóik (JOIN):");
var termekJoin = 
    from t in gyoker.Descendants("termek")
    join sz in gyoker.Descendants("besszallitoceg") on (string)t.Attribute("SzallID") equals (string)sz.Attribute("SzallID")
    select new
    {
        TermekNeve = (string)t.Element("termeknev"),
        Ar = (string)t.Element("Ar_db"),
        Beszallito = (string)sz.Element("nev")
    };

foreach (var item in termekJoin)
{
    Console.WriteLine($"{item.TermekNeve} ({item.Ar} Ft) - Beszállító: {item.Beszallito}");
}

Console.WriteLine("\n(3.) Aggregáció:");
var atlag_ar = gyoker.Descendants("termek")
    .Select(t => t.Element("Ar_db").Value)
    .Average(ar => double.Parse(ar));

Console.WriteLine($"(3.1) A termékek átlagos ára: {atlag_ar} Ft");

Console.WriteLine("\n(4.) Minden termék árát megduplázom, majd elmentem egy új fájlba...");

gyoker.Descendants("termek")
    .ToList()
    .ForEach(t => 
    {
        var arElem = t.Element("Ar_db");
        var ar = double.Parse(arElem.Value);
        ar *= 2;
        arElem.Value = ar.ToString();
    });

XDocument very_italokDokumentum = new XDocument(gyoker);
very_italokDokumentum.Save("draga_italok.xml");

Console.WriteLine("Mentés sikeres: draga_italok.xml");
Console.WriteLine("\n(5.) 40 évnél idősebb patrónusok:");
var idosPatronok = gyoker.Descendants("patron")
    .Where(p => int.Parse(p.Element("kor").Value) > 40)
    .Select(p => $"{p.Element("nev").Element("vezeteknev").Value} {p.Element("nev").Element("keresztnev").Value}");

foreach (var p in idosPatronok) Console.WriteLine($"-- {p}");


Console.WriteLine("\n(6.) Termékek allergének szerint:");
var allergenCsoportok = gyoker.Descendants("termek")
    .GroupBy(t => t.Element("allergen").Value);

foreach (var csoport in allergenCsoportok)
{
    Console.WriteLine($"[{csoport.Key}]");
    foreach (var t in csoport) Console.WriteLine($" - {t.Element("termeknev").Value}");
}

Console.WriteLine("\n(7.) Budapesti dolgozók:");
var bpDolgozok = gyoker.Descendants("dolgozo")
    .Where(d => d.Element("lakcim").Value.Contains("Budapest"))
    .Select(d => $"{d.Element("nev").Element("vezeteknev").Value} {d.Element("nev").Element("keresztnev").Value}");

foreach (var d in bpDolgozok) Console.WriteLine($"-- {d}");

Console.WriteLine("\n(8.) Patrónusok és a bárok, ahova be vannak jelentve:");
var bejelentkezesek = 
    from b in gyoker.Descendants("bejelentve")
    join p in gyoker.Descendants("patron") on (string)b.Attribute("b_p") equals (string)p.Attribute("PatID")
    join l in gyoker.Descendants("luxusbar") on (string)b.Attribute("b_k") equals (string)l.Attribute("BarID")
    select new { Vendeg = (string)p.Element("nev").Element("vezeteknev"), Bar = (string)l.Element("nev") };

foreach (var item in bejelentkezesek) Console.WriteLine($"-- {item.Vendeg} -> {item.Bar}");

Console.WriteLine("\n(9.) Van-e 100 db-nál kevesebb készletű termék?");
bool vanKeves = gyoker.Descendants("termek").Any(t => int.Parse(t.Element("mennyiseg").Value) < 100);
Console.WriteLine($"-- {(vanKeves ? "Igen" : "Nem")}");


Console.WriteLine("\n(10.) Termékek árcsökkenő sorrendben:");
var rendezettTermekek = gyoker.Descendants("termek")
    .OrderByDescending(t => int.Parse(t.Element("Ar_db").Value))
    .Select(t => $"{t.Element("termeknev").Value} - {t.Element("Ar_db").Value} Ft");

foreach (var t in rendezettTermekek) Console.WriteLine($"-- {t}");