import { useState } from 'react'
import './Example.css'

export function V2Example() {
  const [textInputValue, setTextInputValue] = useState('');

  const [items, setItems] = useState([
    'apple',
    'banana',
  ]);

  function handleAddItem(value) {

    // 1.  When i click add item. What will be displayed?
    items.push(value)
    setItems(items);


     // 2. Below is the fix (items not allowed to be modified directly)
     // setItems((items)=> items.concat(value));

  }

  return (
    <div>
      <h1>V2 - Mutate state gotcha!</h1>
      {itemListDisplay(items)}

      <div>
        <input
          value={textInputValue}
          onChange={event => setTextInputValue(event.target.value)}
        />
        <button
          onClick={() => { handleAddItem(textInputValue) }}>Add item</button>
      </div >
    </div>
  )
}


export default V2Example;

function itemListDisplay(items) {
  return <ul>
    {items.map((item, index) => {
      return (
        <li key={index}>
          {item}
        </li>
      );
    })}
  </ul>;
}

