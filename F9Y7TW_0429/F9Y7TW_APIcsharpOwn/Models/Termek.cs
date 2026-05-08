using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;

namespace F9Y7TW_APIcsharpOwn.Models
{
    public class Termek
    {
        [BsonId]
        public ObjectId Id { get; set; }
        public string termeknev { get; set; }
        public string Ar_db { get; set; }
        public string? kezdoar { get; set; }
        public string allergen { get; set; }
        public string mennyiseg { get; set; }
        public string _TermID { get; set; }
        public string _SzallID { get; set; }
    }
}