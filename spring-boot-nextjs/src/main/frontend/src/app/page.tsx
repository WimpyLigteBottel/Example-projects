import CreateButton from "@/component/create-button";
import PersonsComponent from "@/component/persons";

function Home() {
  return (
    <main className="flex min-h-screen flex-col items-center justify-between p-24">
      <PersonsComponent />
      <CreateButton />
    </main>
  );
}

export default Home;
