using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;

namespace F9Y7TW_APIcsharpOwn.Models
{
    public class Luxusbar
    {
        [BsonId]
        public ObjectId Id { get; set; }
        public string nev { get; set; }
        public string varos { get; set; }
        public string _BarID { get; set; }
    }
}