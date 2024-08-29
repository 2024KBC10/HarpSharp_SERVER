import styles from "./task_filter.module.css"

export default function TaskFilter({
    items,
    isOpen,
}: TaskFilterProps) {
    return (
        <ul 
        className={styles.container}
        style={{
            width: "90px",
            marginTop: isOpen ? "40px" : "20px",
            backgroundColor: "#F6F3F2",
            display: isOpen ? "inline-block" : "contents",
            opacity: isOpen ? 1 : 0,
        }}
        >
        {
            isOpen 
            ? items.map((item, index) => (
                <FilterItem
                    key={index}
                    name={item.name} 
                    onClick={item.onClick}
                />
            ))
            : <></>
        }
        </ul>
    )
}

function FilterItem({
    name,
    onClick,
}: FilterItemProps) {
    return (
        <li 
        onClick={() => onClick(name)}
        className={styles.filter_item_wrapper}
        >{name}</li>
    )
}

interface FilterItemProps {
    name: string
    onClick: (name: string) => void
}

interface TaskFilterProps {
    items: FilterItemProps[]
    isOpen: boolean
}