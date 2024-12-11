import "./app.css";

import { StompSessionProvider } from "react-stomp-hooks";

import LogDisplay from "./components/LogDisplay";
import ControlPanel from "./components/ControlPanel";

const Socket = () => {
	return (
		<StompSessionProvider url={"http://localhost:8080/socket-endpoint"}>
			<LogDisplay />
			<ControlPanel />
		</StompSessionProvider>
	);
};

export default Socket;
