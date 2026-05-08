using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;

namespace F9Y7TW_APIcsharpOwn.Models
{
    public class Besszallitoceg
    {
        [BsonId]
        public ObjectId Id { get; set; }
        public string nev { get; set; }
        public string telephely { get; set; }
        public string _SzallID { get; set; }
    }
}