using MongoDB.Driver;
using MongoTest.Models;

class Program {
    static void Main(string[] args)
    {
        var Client = new MongoClient("mongodb+srv://kovacsd435:----@bookdb2026.mbjbb7u.mongodb.net/");
        var database = Client.GetDatabase("vendeglatas");
        var etteremCollection = database.GetCollection<Etterem>("ettermek");
        var foszakacsCollection = database.GetCollection<Foszakacs>("foszakacsok");


        var ettermek = etteremCollection .Find (_ => true ). ToList ();

        foreach (var e in ettermek )
        {
            Console.WriteLine (" -------");
            Console.WriteLine ($"Nev :{e.nev }");
            Console.WriteLine ($" Varos: {e.cim ?. varos }");
            Console.WriteLine ($" Utca: {e.cim ?. utca }");
            Console.WriteLine ($"Street number : {e.cim ?.hazszam }");
            Console.WriteLine ($" Csillag : {e.csillag}");
            }

        var foszakacsok = foszakacsCollection.Find(_ =>true).ToList();
		var reszlegek = szakacsCollection . Find (_ => true ). ToList
        

        foreach (var f in foszakacsok)
        {
            Console.WriteLine("-------");
            Console.WriteLine($"Nev: {f.nev}");
            Console.WriteLine ($"letkor:{f.eletkor}");
            Console.WriteLine($"Fkod:{f._fkod}");
            Console.WriteLine($"EF:{f._e_f}");

            Console.WriteLine("Vegzettseg:");
            foreach (var v in f.vegzettseg)
            {
                Console.WriteLine ("-" + v);
            }
 
        }
        var ujFoszakacs = new Foszakacs
        {
            nev = "Hegedűs Lajos",
            eletkor = 25,
            vegzettseg = new List<string> { "Le Cordon Bleu" },
            _fkod = "f3",
            _e_f = "e1"
        };

        foszakacsCollection.InsertOne(ujFoszakacs);
        Console.WriteLine("Sikeres beszúrás!");       
        

        var filter = Builders<Etterem>.Filter.Eq(e => e.nev, "Valhalla");
        var update = Builders<Etterem>.Update.Set(e => e.csillag, 3);
        etteremCollection.UpdateOne(filter, update);
        Console.WriteLine("Sikeres módosítás!");

        var filter2 = Builders<Foszakacs>.Filter.Lt(f => f.eletkor, 30);
        foszakacsCollection.DeleteMany(filter2);
        Console.WriteLine("Sikeres törlés!");

        var filter3 = Builders<Gyakornok>.Filter.Eq(g => g.nev, "Szilágyi István");
        var update = Builders<Gyakornok>.Update.Push(g => g.muszak, "éjszaka");
        gyakornokCollection.UpdateOne(filter3, update);
        Console.WriteLine("Sikeres hozzáadás!");

        foreach (var sz in reszlegek)
        {
            Console.WriteLine($"{sz.nev} - {sz.reszleg}");
        }

		var result = etteremCollection.Find(e => e.csillag >= 4).ToList();
		foreach (var e in result)
		{
			Console.WriteLine($"Név: {e.nev} - Csillag: {e.csillag}");
		}
		
		var filter = Builders<Etterem>.Filter.Eq(e => e.cim.varos, "Nyíregyháza") | Builders<Etterem>.Filter.Eq(e => e.csillag, 5);
		var result = etteremCollection.Find(filter).ToList();

		foreach (var e in result)
		{
			Console.WriteLine($"{e.nev} - {e.cim.varos} - {e.csillag}");
		}
		
		var result = vendegCollection.Find(v => v.eletkor >= 25 && v.eletkor <= 40).ToList();
		foreach (var v in result)
		{
		Console.WriteLine($"{v.nev} - {v.eletkor}");
		}
		
		
		var result = etteremCollection.Aggregate().Group(e => e.cim.varos, g => new
			{
				Varos = g.Key,
				Darab = g.Count(),
				AtlagCsillag = g.Average(x => x.csillag)
			}).ToList();

		foreach (var r in result)
		{
			Console.WriteLine($"{r.Varos} - db: {r.Darab} - átlag: {r.AtlagCsillag}");
		}
		
		var result = szakacsCollection.Aggregate().Group(s => s._e_sz, g => new
			{
				EtteremKod = g.Key,
				Szam = g.Count()
			}).ToList();

		foreach (var r in result)
		{
			Console.WriteLine($"{r.EtteremKod} - {r.Szam} fő");
		}
		var result = etteremCollection.Aggregate().Lookup("szakacsok", "_ekod", "_e_sz", "szakacsok").ToList();

		foreach (var r in result)
		{
			Console.WriteLine($"Étterem: {r["nev"]}");
			var szakacsok = r["szakacsok"].AsBsonArray;

			foreach (var s in szakacsok)
			{
				Console.WriteLine($"  Szakács: {s["nev"]}");
			}
		}
    }
}