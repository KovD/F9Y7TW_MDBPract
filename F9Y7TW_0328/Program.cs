// using System.Xml.Linq;
// using System.Linq;

// XDocument dokumentum = XDocument.Load("F9Y7TW_XML.xml");
// XElement gyoker = dokumentum.Descendants("vendeglatas").First();

// Console.WriteLine("(0.) A doc \n" + gyoker);

// Console.WriteLine("(1.) Le 5 csillagos édtermek");

// var five_star = gyoker.Descendants("etterem").Where(
//     elem => elem.Descendants("csillag").First().Value == "5").ToList();

// foreach (var item in five_star)
// {
//     Console.WriteLine("--" + item.Descendants("nev").First().Value);
// }

// Console.WriteLine("(3.) Ki, mit mennyiért?");

// var three_join = gyoker.Descendants("rendeles").Select(elem =>
// {
//     var vendegID = elem.Attribute("vkod").Value;
//     var vendeg = gyoker.Descendants("vendeg")
//         .Where(vendegElem => vendegElem.Attribute("vkod").Value == vendegID)
//         .First().Descendants("nev").FirstOrDefault().Value;

//     var etteremID = elem.Attribute("ekod").Value;
//     var etterem = gyoker.Descendants("etterem")
//         .First(etteremElem => etteremElem.Attribute("ekod").Value == etteremID)
//         .Descendants("nev").FirstOrDefault().Value;

//     var rendeltEtel = elem.Descendants("etel").First().Value;
//     var osszeg = elem.Descendants("osszeg").First().Value;

//     return new
//     {
//         Vendeg = vendeg,
//         Etterem = etterem,
//         Etel = rendeltEtel,
//         Osszeg = osszeg
//     };
// }
// ).ToList();

// three_join.ForEach(join => Console.WriteLine(join));

// //or easier if you have new .NET

// Console.WriteLine("-----------------------Easier-----------------------------");

// var harmasJoin = 
//     from r in gyoker.Descendants("rendeles")
//     join v in gyoker.Descendants("vendeg") on (string)r.Attribute("vkod") equals (string)v.Attribute("vkod")
//     join e in gyoker.Descendants("etterem") on (string)r.Attribute("ekod") equals (string)e.Attribute("ekod")
//     select new
//     {
//         VendegNeve = (string)v.Element("nev"),
//         EtteremNeve = (string)e.Element("nev"),
//         Etel = (string)r.Element("etel"),
//         Ar = (string)r.Element("osszeg")
//     };

// foreach (var item in harmasJoin)
// {
//     Console.WriteLine($"{item.VendegNeve} rendelt: {item.Etel} ({item.Ar} Ft) - Helyszín: {item.EtteremNeve}");
// }

// Console.WriteLine("(4.) Aggregáció\n");

// var atlagKoltes = gyoker.Descendants("rendeles")
//     .Select(rendeles => rendeles.Descendants("osszeg").First().Value)
//     .Average(osszeg => double.Parse(osszeg));

// Console.WriteLine($"(4.1) Az átlagos költés: {atlagKoltes}");
// Console.WriteLine("(4.2) Minden rendelés összegét megduplázom, majd elmentem egy új fájlba: \n");

// gyoker.Descendants("rendeles")
//     .ToList()
//     .ForEach (rendeles => {
//         var osszegElem = rendeles.Descendants("osszeg").First();
//         var osszeg = double.Parse(osszegElem.Value);
//         osszeg *= 2;
//         osszegElem.Value = osszeg.ToString();

//     }
// );

// Console.WriteLine(gyoker);

// XDocument modositottDokumentum = new XDocument(gyoker);
// modositottDokumentum.Save("very_drága_libamáj.xml");
