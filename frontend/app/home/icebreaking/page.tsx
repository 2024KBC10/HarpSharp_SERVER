import IBKMainBottom from "./(component)/(bottom)/ibk_main_bottom"
import IBKMainTop from "./(component)/(top)/ibk_main_top"

import styles from "./page.module.css"

export default async function IceBreaking() {
    return (
        <div className={styles.container}>
            <IBKMainTop/>
            <IBKMainBottom/>
        </div>
    )
}