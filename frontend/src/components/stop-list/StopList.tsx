import React from "react";
import IStop from "../../types/IStop";
import "./StopList.css";

const StopList = (props: { stops: IStop[]; showStops: boolean }) => {
  const sortedStopNames = (): string[] => {
    return props.stops.map((stop) => stop.name).sort();
  };

  return (
    <>
      {props.showStops && (
        <ul className="stop-list">
          {sortedStopNames().map((name, index) => (
            <li key={index}>{name}</li>
          ))}
        </ul>
      )}
    </>
  );
};

export default StopList;
