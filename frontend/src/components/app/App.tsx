import React, { useEffect, useState } from "react";
import "./App.css";
import IBusLine from "../../types/IBusLine";
import BusLine from "../bus-line/BusLine";

const App = () => {
  const [busLines, setBusLines] = useState<IBusLine[] | null>(null);

  useEffect(() => {
    fetch("http://localhost:8080/buslines")
      .then((response) => response.json())
      .then((json) => setBusLines(json));
  }, []);

  return (
    <div className="app">
      <h1>Top ten bus lines with most amount of stops on their route</h1>
      <div className="container">
        {busLines ? (
          busLines.map((busLine, index) => (
            <BusLine
              busLine={busLine}
              topTenPlacement={index + 1}
              key={index}
            />
          ))
        ) : (
          <p>Loading...</p>
        )}
      </div>
    </div>
  );
};

export default App;
