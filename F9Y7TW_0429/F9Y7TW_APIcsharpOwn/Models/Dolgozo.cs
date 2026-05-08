using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;

namespace F9Y7TW_APIcsharpOwn.Models
{
    public class Dolgozo
    {
        [BsonId]
        public ObjectId Id { get; set; }
        public string nev_vezeteknev { get; set; } // A JSON-ben összetett objektum van, de egyszerűbb stringként kezelni, ha átalakítod, vagy...
        
        // Ha maradni akarsz a JSON szerkezetnél (név objektum):
        public Nev nev { get; set; }
        public string kor { get; set; } // A JSON-ben stringként szerepel ("30")
        public string dolgozo_email_cim { get; set; }
        public string _SzemID { get; set; }
    }

    public class Nev 
    {
        public string keresztnev { get; set; }
        public string vezeteknev { get; set; }
        public string titulus { get; set; }
    }
}