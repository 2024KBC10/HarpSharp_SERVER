import styles from "./loading.module.css"

export default function LoadingIndicator({
    size = 30,
    spinnerCount = 1,
}: LoadingIndicatorProps) {
    return (
        <div 
        className={styles.container}
        style={{ width: `${size}px`, height: `${size}px` }}
        >
            <div 
            className={styles.spinner}
            style={{
                borderTop: "4px solid #5A77F6",
                borderBottom: spinnerCount === 2 ? "4px solid #5A77F6" : undefined,
            }}
            />
        </div>
    )
}

interface LoadingIndicatorProps {
    size?: number
    spinnerCount?: 1 | 2
}