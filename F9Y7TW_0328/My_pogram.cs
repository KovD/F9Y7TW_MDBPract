using System.Xml.Linq;
using System.Linq;

XDocument dokumentum = XDocument.Load("F9Y7TW_XML1.xml");
XElement gyoker = dokumentum.Descendants("vendeglatas").First();

Console.WriteLine(gyoker);