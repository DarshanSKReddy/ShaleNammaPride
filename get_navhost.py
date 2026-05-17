with open("app/src/main/java/com/example/shale_nammapride/MainActivity.kt", "r") as f:
    text = f.read()

start = text.find("NavHost")
print(text[start:start+1000])
