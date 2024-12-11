import { useState } from "react";
import { useSubscription } from "react-stomp-hooks";

const LogDisplay = () => {
	const [log, setLog] = useState("No log has been received yet");

	useSubscription("/topic/logs", (message) => setLog(message.body));

	return <div className="log-box">System log: {log}</div>;
};

export default LogDisplay;
