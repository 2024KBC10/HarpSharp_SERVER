import FocusTask from "./(component)/(focustask)";
import MyActivity from "./(component)/(myactivity)";
import MyTasks from "./(component)/(mytasks)";

import styles from "./page.module.css";

export default function Home() {
  return (
    <div className={styles.container}>
      <FocusTask/>
      <div className={styles.row_container}>
        <MyActivity/>
        <MyTasks/>
      </div>
    </div>
  );
}
