import pymongo as mongo

client = mongo.MongoClient("mongodb+srv://kovacsd435:-----@bookdb2026.mbjbb7u.mongodb.net/?appName=BookDB2026")
db = client["vendeglatasAttilla"]

etterem_coll = db["etterem"]
foszakacs_coll = db["foszakacs"]

etterem_coll.delete_many({})
foszakacs_coll.delete_many({})


ettermek_adatok = [
    {
        "_id": "e1",
        "nev": "Aranyhal Étterem",
        "cim": {
            "varos": "Miskolc",
            "utca": "Széchenyi u.",
            "hazszam": 107
        },
        "csillag": 3
    },
    {
        "_id": "e2",
        "nev": "Kovács Dániel very good lokin restaurant",
        "cim": {
            "varos": "Kassa",
            "utca": "Petőfi Antal köz",
            "hazszam": 408
        },
        "csillag": 4
    },
    {
        "_id": "e3",
        "nev": "Creppy Palacsintaház Étterem",
        "cim": {
            "varos": "Miskolc",
            "utca": "Méylvölgy utca",
            "hazszam": 15
        },
        "csillag": 5
    }
]


foszakacsok = [
    {
        "_id": "f1",
        "e_f": "e1",
        "nev": "Kovács Dániel",
        "eletkor": 45,
        "vegzettseg":["Szakközépiskola", "Főiskola"]
    },
    {
        "_id": "f2",
        "e_f": "e2",
        "nev": "Kovács János",
        "eletkor": 38,
        "vegzettseg": ["Szakközépiskola", "Főiskola"]
    },
    {
        "_id": "f3",
        "e_f": "e3",
        "nev": "Nemes Géza",
        "eletkor": 28,
        "vegzettseg": ["Főiskola"]
    }
]
foszakacs_coll.insert_many(foszakacsok)
etterem_coll.insert_many(ettermek_adatok)
print("Főszakácsok feltöltve.")

for etterem in etterem_coll.find():
    print(etterem)

print("------------------------------------------------------------")
for foszakacs in foszakacs_coll.find():
    print(foszakacs)
print("------------------------------------------------------------")
e2_etterem = etterem_coll.find_one({"_id": "e1"})
print(e2_etterem)

print("------------------------------------------------------------")
for etterem_csillag in etterem_coll.find({"csillag": {"$lte": 4}}):
    print(etterem_csillag)

print("-------------------------------------------------------------")
pipeline_avg = [
    {
        "$group": {
            "_id": None,
            "atlagEletkor": {"$avg": "$eletkor"}
        }
    }
]
atlag_eredmeny = list(foszakacs_coll.aggregate(pipeline_avg))

atlag = atlag_eredmeny[0]['atlagEletkor']
print(f"A főszakácsok átlagos életkora: {atlag:.2f} év")
print("--------------------------------------------------------")
pipeline = [
    {
        "$match": {
            "vegzettseg": "Szakközépiskola"
        }
    },
    {
        "$lookup": {
            "from": "etterem",
            "localField": "e_f",
            "foreignField": "_id",
            "as": "etterem_adatok"
        }
    }
]

for doc in foszakacs_coll.aggregate(pipeline):
    e_nev = doc['etterem_adatok'][0]['nev'] if doc['etterem_adatok'] else "Nincs adat"
    print(f"Főszakács: {doc['nev']}----------------- Étterem: {e_nev}")

print("---------------------------Update.-------------------------------")
print("\n--- 3.a)e1-es ID étterem módosítása ---")
etterem_coll.update_one(
    {"_id": "e1"},              
    {"$set": {"csillag": 4}}    
)

for etterem in etterem_coll.find():
    print(etterem)

print("--------------------------------------------------------")
torles_f2 = foszakacs_coll.delete_one(
    {"_id": "f2"})
print("debug_log")
for f in foszakacs_coll.find():
    print(f)

print("--------------------------------------------------------")
torles_eredmeny = foszakacs_coll.delete_many(
    {"eletkor": {"$lt": 30}}
)
print(f"Törölt: {torles_eredmeny.deleted_count}")
for f in foszakacs_coll.find():
    print(f)