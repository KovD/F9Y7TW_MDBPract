using MongoDB.Driver;
using MongoDB.Bson;
using F9Y7TW_APIcsharpOwn.Models;

class Program {
    static void Main(string[] args)
    {
        var client = new MongoClient("mongodb+srv://kovacsd435:------@bookdb2026.mbjbb7u.mongodb.net/");
        var database = client.GetDatabase("luxusbarDB");


        var barCollection = database.GetCollection<Luxusbar>("luxusbar");
        var termekCollection = database.GetCollection<Termek>("termek");
        var dolgozoCollection = database.GetCollection<Dolgozo>("dolgozo");

        var barok = barCollection.Find(_ => true).ToList();
        Console.WriteLine("--- LUXUSBÁROK ---");
        foreach (var b in barok)
        {
            Console.WriteLine($"{b.nev} ({b.varos}) - ID: {b._BarID}");
        }

        var ujTermek = new Termek
        {
            termeknev = "Kézműves Gin",
            Ar_db = "15000",
            allergen = "Nincs",
            kezdoar = "11000",
            mennyiseg = "40",
            _TermID = "t5",
            _SzallID = "sz1"
        };
        termekCollection.InsertOne(ujTermek);
        Console.WriteLine("\nSikeres termék beszúrás!");

        var filterUpdate = Builders<Termek>.Filter.Eq(t => t.termeknev, "Prémium Vodka");
        var updateAction = Builders<Termek>.Update.Set(t => t.Ar_db, "13500");
        termekCollection.UpdateOne(filterUpdate, updateAction);
        Console.WriteLine("Prémium Vodka ára frissítve.");

        var filterDelete = Builders<Dolgozo>.Filter.Lt(d => d.kor, "26");
        dolgozoCollection.DeleteMany(filterDelete);
        Console.WriteLine("26 év alatti dolgozók eltávolítva.");

        var filterOr = Builders<Luxusbar>.Filter.Eq(b => b.varos, "Budapest") | 
                        Builders<Luxusbar>.Filter.Eq(b => b.varos, "Debrecen");
        var kiemeltBarok = barCollection.Find(filterOr).ToList();
        
        Console.WriteLine("\nBudapesti és Debreceni bárok:");
        foreach (var b in kiemeltBarok)
        {
            Console.WriteLine($"- {b.nev}");
        }

        var varosStat = barCollection.Aggregate().Group(b => b.varos, g => new
            {
                Helyszin = g.Key,
                BarokSzama = g.Count()
            }).ToList();

        Console.WriteLine("\nVáros statisztika:");
        foreach (var s in varosStat)
        {
            Console.WriteLine($"{s.Helyszin}: {s.BarokSzama} db bár");
        }

        var termekBeszerzes = termekCollection.Aggregate().Lookup("besszallitoceg", "_SzallID", "_SzallID", "beszallito_info").ToList();

        Console.WriteLine("\nTermékek és beszállítóik:");
        foreach (var doc in termekBeszerzes)
        {
            Console.WriteLine($"Termék: {doc["termeknev"]}");
            var info = doc["beszallito_info"].AsBsonArray;
            foreach (var ceg in info)
            {
                Console.WriteLine($"  Beszállító: {ceg["nev"]} ({ceg["telephely"]})");
            }
        }
    }
}