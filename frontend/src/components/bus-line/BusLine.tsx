import React, { useState } from "react";
import IBusLine from "../../types/IBusLine";
import StopList from "../stop-list/StopList";

import "./BusLine.css";

const BusLine = (props: { busLine: IBusLine; topTenPlacement: number }) => {
  const [showStops, setShowStops] = useState(false);

  return (
    <div className="bus-line">
      <div className="bus-line__container">
        <p className="bus-line__top-ten-placement">{props.topTenPlacement}.</p>
        <p>
          {props.busLine.lineNumber} ({props.busLine.stops.length} stops)
        </p>
        <button onClick={() => setShowStops(!showStops)}>
          {showStops ? "Hide" : "Show"} stops
        </button>
      </div>
      <StopList stops={props.busLine.stops} showStops={showStops} />
    </div>
  );
};

export default BusLine;
