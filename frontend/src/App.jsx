import { useEffect, useState } from "react";
import axios from "axios";

function App() {
  const [earthquakes, setEarthquakes] = useState([]);
  const [timeFilter, setTimeFilter] = useState("");
  const [message, setMessage] = useState("");

  const API = "http://localhost:8081/api/earthquakes";

  const loadEarthquakes = async () => {
    try {
      const response = await axios.get(API);
      setEarthquakes(response.data);
      setMessage("");
    } catch (error) {
      setMessage("Error loading data.");
      console.error(error);
    }
  };

  const fetchLatest = async () => {
    try {
      const response = await axios.get(`${API}/fetch`);
      setMessage(response.data);
      await loadEarthquakes();
    } catch (error) {
      setMessage("Error fetching latest data.");
      console.error(error);
    }
  };

  const formatTimeForApi = (value) => {
    if (!value) return "";
    return value.length === 16 ? `${value}:00` : value;
  };

  const filterByTime = async () => {
    if (!timeFilter) {
      await loadEarthquakes();
      return;
    }

    try {
      const formattedTime = formatTimeForApi(timeFilter);
      const response = await axios.get(`${API}/after`, {
        params: { time: formattedTime },
      });
      setEarthquakes(response.data);
      setMessage("");
    } catch (error) {
      setMessage("Error filtering data.");
      console.error(error);
    }
  };

  const clearFilter = async () => {
    setTimeFilter("");
    await loadEarthquakes();
  };

  const deleteItem = async (id) => {
    try {
      await axios.delete(`${API}/${id}`);
      setMessage("Record deleted successfully.");
      await loadEarthquakes();
    } catch (error) {
      setMessage("Error deleting record.");
      console.error(error);
    }
  };

  useEffect(() => {
    const fetchInitialData = async () => {
      try {
        const response = await axios.get(API);
        setEarthquakes(response.data);
        setMessage("");
      } catch (error) {
        setMessage("Error loading data.");
        console.error(error);
      }
    };

    fetchInitialData();
  }, []);

  return (
      <div className="container mt-4">
        <div className="row justify-content-center">
          <div className="col-lg-10">
            <h3 className="text-center mb-4">Earthquake Monitoring System</h3>

            <div className="card border-0 shadow-sm mb-4">
              <div className="card-body">
                <div className="d-flex flex-wrap gap-2 align-items-center">
                  <button className="btn btn-primary btn-sm" onClick={fetchLatest}>
                    Fetch latest
                  </button>

                  <button
                      className="btn btn-outline-secondary btn-sm"
                      onClick={loadEarthquakes}
                  >
                    Reload
                  </button>

                  <input
                      type="datetime-local"
                      className="form-control form-control-sm"
                      style={{ maxWidth: "220px" }}
                      value={timeFilter}
                      onChange={(e) => setTimeFilter(e.target.value)}
                  />

                  <button className="btn btn-success btn-sm" onClick={filterByTime}>
                    Filter
                  </button>

                  <button className="btn btn-outline-dark btn-sm" onClick={clearFilter}>
                    Clear
                  </button>
                </div>
              </div>
            </div>

            {message && <div className="alert alert-info py-2">{message}</div>}

            <div className="table-responsive">
              <table className="table table-bordered table-sm align-middle">
                <thead className="table-light">
                <tr>
                  <th>ID</th>
                  <th>Magnitude</th>
                  <th>Place</th>
                  <th>Title</th>
                  <th>Time</th>
                  <th></th>
                </tr>
                </thead>

                <tbody>
                {earthquakes.length > 0 ? (
                    earthquakes.map((eq) => (
                        <tr key={eq.id}>
                          <td>{eq.id}</td>
                          <td>{eq.magnitude}</td>
                          <td>{eq.place}</td>
                          <td>{eq.title}</td>
                          <td>{new Date(eq.eventTime).toLocaleString()}</td>
                          <td>
                            <button
                                className="btn btn-outline-danger btn-sm"
                                onClick={() => deleteItem(eq.id)}
                            >
                              Delete
                            </button>
                          </td>
                        </tr>
                    ))
                ) : (
                    <tr>
                      <td colSpan="6" className="text-center">
                        No data available.
                      </td>
                    </tr>
                )}
                </tbody>
              </table>
            </div>

            <div className="mt-2 text-muted small">
              Stored data depends on the current USGS hourly feed and the magnitude greater than 2.0 filter.
            </div>
          </div>
        </div>
      </div>
  );
}

export default App;